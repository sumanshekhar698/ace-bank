<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Get Started | AceBank</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
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
                    <li><a href="login.jsp">Login</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="auth-card">
        <div class="auth-header">
            <h2>Create Account</h2>
            <p>Join thousands of users managing money smarter.</p>
        </div>

        <form action="SignUp" method="POST" id="signup-form">
            <div class="input-row">
                <div class="form-control" id="firstName-group">
                    <input type="text" name="firstName" id="firstName" placeholder="First Name">
                    <i class="material-icons status-icon">check_circle</i>
                    <small class="error-msg">Error message</small>
                </div>
                <div class="form-control" id="lastName-group">
                    <input type="text" name="lastName" id="lastName" placeholder="Last Name">
                    <i class="material-icons status-icon">check_circle</i>
                    <small class="error-msg">Error message</small>
                </div>
            </div>

            <div class="form-control" id="aadhar-group">
                <input type="text" name="aadharNumber" id="aadharNumber" placeholder="Aadhar Number (12 digits)">
                <i class="material-icons status-icon">check_circle</i>
                <small class="error-msg">Error message</small>
            </div>

            <div class="form-control" id="email-group">
                <input type="email" name="email" id="email" placeholder="Email Address">
                <i class="material-icons status-icon">check_circle</i>
                <small class="error-msg">Error message</small>
            </div>

            <div class="form-control" id="password-group">
                <input type="password" name="password" id="password" placeholder="Password (Min. 10 chars)">
                <i class="material-icons status-icon">check_circle</i>
                <small class="error-msg">Error message</small>
            </div>

            <button type="submit" id="submit-btn" class="btn-primary full-width">Create Account</button>
        </form>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/sign-up.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/dark-mode.js"></script>

</body>
</html>