package dev.codecounty.acebank.controllers;

import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

import dev.codecounty.acebank.model.Transaction;

import dev.codecounty.acebank.dao.BankUserDao;
import dev.codecounty.acebank.dao.BankUserDaoImpl;
import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;

@Log
@WebServlet(name = "Login", urlPatterns = "/Login")
public class Login extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    // FIX: Initialize the Service Layer
    private final BankService bankService = new BankServiceImpl();
    private final BankUserDao userDao = new BankUserDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accStr = request.getParameter("accountNumber");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        try {
            int accountNo = Integer.parseInt(accStr);

            // 1. Authenticate via Service
            var loginResultOpt = bankService.authenticate(accountNo, password);

            if (loginResultOpt.isPresent()) {
                var details = loginResultOpt.get();
                HttpSession session = request.getSession(true);

                // 2. Populate Session Attributes
                session.setAttribute("accountNumber", accountNo);
                session.setAttribute("firstName", details.firstName());
                session.setAttribute("lastName", details.lastName());
                session.setAttribute("email", details.email());
                session.setAttribute("balance", details.balance());

                // 3. Fetch Transaction History for the Dashboard
                List<Transaction> statement = userDao.getStatement(accountNo);
                session.setAttribute("transactionDetailsList", statement);

                // 4. Handle "Remember Me" Cookie
                Cookie userCookie = new Cookie("rememberedAccount", accStr);
                userCookie.setHttpOnly(true); // Security: Prevents JS access

                if ("on".equals(rememberMe)) {
                    userCookie.setMaxAge(60 * 60 * 24 * 30); // 30 Days
                } else {
                    userCookie.setMaxAge(0); // Delete cookie if not checked
                }
                response.addCookie(userCookie);

                log.info("User " + accountNo + " logged in successfully.");
                request.getRequestDispatcher("/WEB-INF/views/Home.jsp").forward(request, response);
            } else {
                log.warning("Authentication failed for account: " + accStr);
                response.sendRedirect("LoginFail.jsp");
            }
        } catch (Exception e) {
            log.severe("Login Error: " + e.getMessage());
            response.sendRedirect("LoginFail.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("Login.jsp");
    }
}