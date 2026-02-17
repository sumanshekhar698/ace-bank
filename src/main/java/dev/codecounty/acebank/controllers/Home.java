package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.model.ServiceResponse;
import dev.codecounty.acebank.model.Transaction;
import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Log
@WebServlet("/HomeAction") // Changed URL to avoid conflict with Home.jsp
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int accountNumber = (int) session.getAttribute("accountNumber");
        String depositAmtStr = request.getParameter("deposit");
        String toAccountStr = request.getParameter("toAccount");
        String toAmountStr = request.getParameter("toAmount");

        try {
            // --- CASE 1: DEPOSIT ---
            if (depositAmtStr != null && !depositAmtStr.isEmpty()) {
                BigDecimal amount = new BigDecimal(depositAmtStr);
                log.info("Deposit initiated for: " + accountNumber);

                if (bankService.processDeposit(accountNumber, amount)) {
                    updateSessionData(session, accountNumber);
                    response.sendRedirect("Home.jsp?msg=Deposit+Successful");
                } else {
                    response.sendRedirect("Home.jsp?error=Deposit+Failed");
                }
            }

            // --- CASE 2: TRANSFER ---
            else if (toAccountStr != null && toAmountStr != null) {
                int recipientAcc = Integer.parseInt(toAccountStr);
                BigDecimal amount = new BigDecimal(toAmountStr);
                log.info("Transfer initiated from " + accountNumber + " to " + recipientAcc);

                ServiceResponse result = bankService.processTransfer(accountNumber, recipientAcc, amount);
                if (result.success()) {
                    response.sendRedirect("Home.jsp?msg=" + result.message());
                } else {
                    response.sendRedirect("Home.jsp?error=" + result.message());
                }
            } else {
                response.sendRedirect("Home.jsp");
            }

        } catch (Exception e) {
            log.severe("Error in Home Action: " + e.getMessage());
            response.sendRedirect("Home.jsp?error=Invalid+Input");
        }
    }

    // Helper method to refresh session data after any transaction
    private void updateSessionData(HttpSession session, int accountNumber) {
        // Fetch updated balance and transaction list from the service
        BigDecimal newBalance = bankService.getBalance(accountNumber);
        List<Transaction> newList = bankService.getTransactionHistory(accountNumber);

        session.setAttribute("balance", newBalance);
        session.setAttribute("transactionDetailsList", newList);
    }
}