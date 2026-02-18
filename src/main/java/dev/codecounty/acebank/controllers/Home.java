package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.model.ServiceResponse;
import dev.codecounty.acebank.model.Transaction;
import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Log
@WebServlet("/home") // This is the single entry point for the Dashboard
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BankService bankService = new BankServiceImpl();

    /**
     * GET: Responsible for fetching data and showing the dashboard.
     * Accessing this directly or via redirect will show the Home.jsp.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Security Check
        if (session == null || session.getAttribute("accountNumber") == null) {
            log.warning("Unauthorized access attempt to /home");
            response.sendRedirect("login.jsp");
            return;
        }

        int accountNumber = (int) session.getAttribute("accountNumber");

        try {
            // Always refresh data before painting the UI
            updateSessionData(session, accountNumber);

            // Forward to the hidden JSP in WEB-INF
            request.getRequestDispatcher("/WEB-INF/views/Home.jsp").forward(request, response);

        } catch (Exception e) {
            log.severe("Error rendering Home Dashboard: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Dashboard load failed.");
        }
    }

    /**
     * POST: Responsible for processing Actions (Deposit/Transfer).
     * After processing, it REDIRECTS to the GET method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int accountNumber = (int) session.getAttribute("accountNumber");

        // Get form parameters
        String depositAmtStr = request.getParameter("deposit");
        String toAccountStr = request.getParameter("toAccount");
        String toAmountStr = request.getParameter("toAmount");

        String redirectUrl = "home"; // Default redirect back to dashboard

        try {
            // --- ACTION 1: DEPOSIT ---
            if (depositAmtStr != null && !depositAmtStr.trim().isEmpty()) {
                BigDecimal amount = new BigDecimal(depositAmtStr);
                if (bankService.processDeposit(accountNumber, amount)) {
                    redirectUrl += "?msg=Deposit+of+₹" + amount + "+Successful";
                } else {
                    redirectUrl += "?error=Deposit+Failed";
                }
            }

            // --- ACTION 2: TRANSFER ---
            else if (toAccountStr != null && toAmountStr != null && !toAccountStr.isEmpty()) {
                int recipientAcc = Integer.parseInt(toAccountStr);
                BigDecimal amount = new BigDecimal(toAmountStr);

                ServiceResponse result = bankService.processTransfer(accountNumber, recipientAcc, amount);
                if (result.success()) {
                    redirectUrl += "?msg=" + result.message();
                } else {
                    redirectUrl += "?error=" + result.message();
                }
            }

        } catch (NumberFormatException e) {
            redirectUrl += "?error=Invalid+Amount+Format";
        } catch (Exception e) {
            log.severe("Transaction Error: " + e.getMessage());
            redirectUrl += "?error=Transaction+could+not+be+completed";
        }

        // The Redirect: This triggers the doGet() and prevents double-form submission
        response.sendRedirect(redirectUrl);
    }

    private void updateSessionData(HttpSession session, int accountNumber) {
        log.info("Refreshing session data for account: " + accountNumber);
        BigDecimal newBalance = bankService.getBalance(accountNumber);
        List<Transaction> newList = bankService.getTransactionHistory(accountNumber);

        session.setAttribute("balance", newBalance);
        session.setAttribute("transactionDetailsList", newList);
    }
}