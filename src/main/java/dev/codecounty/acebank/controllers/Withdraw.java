package dev.codecounty.acebank.controllers;


import dev.codecounty.acebank.service.BankService;
import dev.codecounty.acebank.service.BankServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/Withdraw")
public class Withdraw extends HttpServlet {
    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Retrieve account number from session (set during login)
        Object accObj = req.getSession().getAttribute("accountNumber");
        
        if (accObj == null) {
            resp.sendRedirect("Login.jsp");
            return;
        }

        int accNo = (int) accObj;
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));

        // The "Brain" (Service) handles the logic
        String result = bankService.withdraw(accNo, amount);

        if ("SUCCESS".equals(result)) {
            resp.sendRedirect("Home.jsp?msg=WithdrawalSuccessful");
        } else {
            req.getSession().setAttribute("errorMessage", result);
            resp.sendRedirect("Withdraw.jsp");
        }
    }
}