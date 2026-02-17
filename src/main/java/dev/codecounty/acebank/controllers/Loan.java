package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
@WebServlet(name = "Loan", urlPatterns = "/Loan")
public class Loan extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Safety Check: Is user logged in?
        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        String loanType = request.getParameter("loanType");
        String firstName = (String) session.getAttribute("firstName");
        String email = (String) session.getAttribute("email");

        log.info("Processing " + loanType + " application for: " + email);

        // Call the Service Layer to handle the "Business Logic" of the loan application
        boolean isApplied = bankService.applyForLoan(firstName, email, loanType);

        if (isApplied) {
            response.sendRedirect("LoanThankYou.jsp");
        } else {
            log.warning("Loan application failed for: " + email);
            response.sendRedirect("GenericError.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Redirect direct URL access back to the options page
        response.sendRedirect("LoanOptions.jsp");
    }
}