package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/Deposit")
public class Deposit extends HttpServlet {
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer accNo = (Integer) session.getAttribute("accountNumber");
        String amountStr = request.getParameter("amount");

        if (accNo == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            
            // 1. Call Service to handle the deposit
            boolean success = bankService.processDeposit(accNo, amount);

            if (success) {
                // 2. Update session balance so user sees the change immediately
                BigDecimal currentBalance = (BigDecimal) session.getAttribute("balance");
                session.setAttribute("balance", currentBalance.add(amount));
                
                // 3. Optional: Refresh transaction list here or in a filter
                response.sendRedirect("Home.jsp?msg=Deposit+Successful");
            } else {
                response.sendRedirect("Home.jsp?msg=Deposit+Failed");
            }
        } catch (Exception e) {
            response.sendRedirect("Home.jsp?msg=Invalid+Amount");
        }
    }
}