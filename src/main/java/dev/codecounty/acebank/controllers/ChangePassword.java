package dev.codecounty.acebank.controllers;

import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.sql.SQLException;

@Log
@WebServlet(name = "ChangePassword", urlPatterns = "/ChangePassword")
public class ChangePassword extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        HttpSession session = request.getSession(false);

        // Guard Clause: Ensure session exists
        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        int accountNo = (int) session.getAttribute("accountNumber");

        // Use the Service Layer to handle logic (Verification + Hashing + DB Update)
        boolean isChanged = false;
        try {
            isChanged = bankService.changePassword(accountNo, currentPassword, newPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (isChanged) {
            log.info("Password successfully changed for Account: " + accountNo);

            // Redirect to Logout with a message.
            // Redirecting is cleaner than forwarding as it prevents double-submission.
            response.sendRedirect("Logout?msg=PasswordChangedSuccessfully");
        } else {
            log.warning("Password change failed for Account: " + accountNo + " (Invalid Current Password)");
            response.sendRedirect("ChangePassword.jsp?error=invalid_current_password");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("ChangePassword.jsp");
    }
}