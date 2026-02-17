package dev.codecounty.acebank.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/Home.jsp", "/Withdraw", "/Transfer", "/getStatement"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Check if the user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("accountNumber") != null);

        if (isLoggedIn) {
            // User is authenticated, pass the request along the chain
            chain.doFilter(request, response);
        } else {
            // User is not logged in, kick them back to the login page
            // We use a query param so the Login page can show a "Please login first" message
            httpResponse.sendRedirect("login.jsp?error=unauthorized");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}