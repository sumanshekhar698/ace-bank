package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.model.ServiceResponse;
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

@Log
@WebServlet("/Transfer")
public class Transfer extends HttpServlet {
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Get Sender Info from Session
        Integer fromAccount = (Integer) session.getAttribute("accountNumber");
        if (fromAccount == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // 2. Get Recipient Info from Form
        String toAccStr = request.getParameter("toAccount");
        String amountStr = request.getParameter("toAmount");

        try {
            int toAccount = Integer.parseInt(toAccStr);
            BigDecimal amount = new BigDecimal(amountStr);

            // 3. Call Service Layer
            // The service returns a message (e.g., "SUCCESS", "Insufficient Funds", etc.)
            ServiceResponse result = bankService.processTransfer(fromAccount, toAccount, amount);


            if (result.success()) {
                // 4. Update the session balance so the Home page shows the new amount immediately
                BigDecimal currentBalance = (BigDecimal) session.getAttribute("balance");
                session.setAttribute("balance", currentBalance.subtract(amount));
                response.sendRedirect("Home.jsp?msg=" + result.message());
            } else {
                response.sendRedirect("Home.jsp?msg=" + result.message());
            }


        } catch (NumberFormatException e) {
            log.warning("Invalid transfer input: " + toAccStr);
            response.sendRedirect("Home.jsp?msg=Invalid+Input");
        }
    }
}