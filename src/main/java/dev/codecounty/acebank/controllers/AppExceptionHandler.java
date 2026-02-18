package dev.codecounty.acebank.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.Serial;

@Log
@WebServlet("/AppExceptionHandler")
public class AppExceptionHandler extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");

        String title;
        String message;

        if (statusCode == 404) {
            title = "Page Not Found";
            message = "The URL <strong>" + requestUri + "</strong> does not exist on our server.";
            log.warning("404 Error: " + requestUri);
        } else if (statusCode == 500) {
            title = "Server Error";
            message = "Our banking systems are experiencing a temporary hiccup. Our engineers have been notified.";
            log.severe("500 Error: " + (throwable != null ? throwable.getMessage() : "Unknown"));
        } else {
            title = "Unexpected Error";
            message = "An unexpected error occurred. Please try again later.";
        }

        // Pass data to the JSP for rendering
        request.setAttribute("errorTitle", title);
        request.setAttribute("errorMessage", message);
        request.setAttribute("errorCode", statusCode);

        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}