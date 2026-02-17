<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // Keeping the cookie logic but making it accessible to EL
    String savedAccount = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("rememberedAccount".equals(c.getName())) {
                savedAccount = c.getValue();
                pageContext.setAttribute("savedAccount", savedAccount);
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | AceBank</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/remixicon/3.5.0/remixicon.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">

</head>

<body class="${cookie.theme_pref.value}">
<div class="auth-wrapper fade-in-up">

    <header>
        <div class="logo">
            <h1 class="logo-text">Ace<span>Bank</span></h1>
        </div>

        <div class="header-actions">


            <%--            <input type="checkbox" id="nav-toggle" class="nav-toggle">--%>
            <%--            <label for="nav-toggle" class="nav-toggle-label">--%>
            <%--                <i class="ri-menu-5-line"></i>--%>
            <%--            </label>--%>

            <nav>
                <ul>
                    <div class="theme-switch-wrapper">
                        <label class="theme-switch" for="theme-checkbox">
                            <input type="checkbox" id="theme-checkbox"/>
                            <div class="slider round">
                                <i class="ri-sun-line"></i>
                                <i class="ri-moon-line"></i>
                            </div>
                        </label>
                    </div>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="sign-up.jsp">Join Now</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="auth-card">
        <div class="auth-header">
            <h2>Welcome Back</h2>
            <p>Enter your credentials to access your account.</p>
        </div>

        <form action="Login" method="POST">
            <div class="form-control">
                <label for="accNo">Account Number</label>
                <input type="text" id="accNo" name="accountNumber"
                       value="${savedAccount}" required
                       placeholder="Enter Account Number">
            </div>

            <div class="form-control">
                <label for="pass">Password</label>
                <input type="password" id="pass" name="password"
                       required placeholder="••••••••">
            </div>

            <div class="form-options">
                <div class="remember-me">
                    <input type="checkbox" name="rememberMe" id="remember"
                    ${not empty savedAccount ? 'checked' : ''}>
                    <label for="remember">Remember Me</label>
                </div>
                <a href="ForgotPassword.jsp" class="forgot-link">Forgot Password?</a>
            </div>

            <button type="submit" class="btn-primary full-width">Login to Account</button>
        </form>

        <p class="auth-footer">
            New to Ace Bank? <a href="sign-up.jsp">Create an account</a>
        </p>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/dark-mode.js"></script>

</body>
</html>