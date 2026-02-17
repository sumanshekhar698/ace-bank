package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import java.io.IOException;

@Log
@WebServlet(name = "Forgot", urlPatterns = "/Forgot")
public class Forgot extends HttpServlet {
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        // The Service Layer handles the User + Account record logic
        if (bankService.recoverAccount(email)) {
            log.info("Recovery success for: " + email);
            response.sendRedirect("Login.jsp?msg=Details+Sent+To+Email");
        } else {
            log.warning("Recovery failed: Email not found - " + email);
            response.sendRedirect("ForgotFail.jsp");
        }
    }
}