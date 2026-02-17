<%--
  Created by IntelliJ IDEA.
  User: suman
  Date: 08-02-2026
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="./css/navbar-home.css">
    <link rel="stylesheet" type="text/css" href="./css/account.css">
    <title>Change Password</title>
</head>

<body>
<header>
    <h1 class="logo-text">Ace<span class="bank">Bank</span></h1>
    <!-- Navigation toggle -->
    <input type="checkbox" id="nav-toggle" class="nav-toggle">
    <label for="nav-toggle" class="nav-toggle-label">
        <img src="images\menu-24px.svg">
    </label>
    <nav>
        <ul>
            <!-- Using Home.jsp directly instead of Home controller because it gives a blank page otherwise -->
            <li><a href="Home.jsp">Dashboard</a></li>
            <li><a href="index.jsp" class="login-btn">Logout</a></li>
        </ul>
    </nav>
</header>
<!--  -->
<div class="container">
    <main class="settings-container">
        <div class="title-container">
            <h1>Change Password</h1>
            <small>Password should at least be 10 characters long</small>
        </div>
        <div class="change-container">
            <form action="ChangePassword" id="pass-form" method="post">
                <div class="form-control">
                    <label>Enter Current Password</label>
                    <input type="text" name="currentPassword" id="currentPassword" placeholder="Current Password"
                           required onfocusout="validatePassword1()">
                    <i class="material-icons" id="correct">check_circle</i>
                    <i class="material-icons" id="error">error</i>
                    <small>Error message</small>
                </div>

                <div class="form-control">
                    <label>Enter New Password</label>
                    <input type="text" name="newPassword" id="newPassword" placeholder="New Password" required
                           onfocusout="validatePassword1()">
                    <i class="material-icons" id="correct">check_circle</i>
                    <i class="material-icons" id="error">error</i>
                    <small>Error message</small>
                </div>

                <div class="form-control">
                    <label>Confirm New Password</label>
                    <input type="text" name="confirmNewPassword" id="confirmNewPassword"
                           placeholder="Confirm New Password" required onfocusout="validatePassword2()">
                    <i class="material-icons" id="correct">check_circle</i>
                    <i class="material-icons" id="error">error</i>
                    <small>Error message</small>
                </div>
                <input type="submit">
            </form>
        </div>
    </main>
</div>
<script>
    function validatePassword2() {
        const errorElement = document.getElementById("error");
        const newPassword = document.getElementById("newPassword").value;
        const confirmNewPassword = document.getElementById("confirmNewPassword").value;

        if (newPassword !== confirmNewPassword) {
            errorElement.classList.add("show");
            errorElement.innerHTML = "Passwords do not match";
        } else {
            errorElement.classList.remove("show");
        }
    }
    document.getElementById("pass-form").addEventListener("submit", validatePassword2);
</script>
<script defer src="./javascript/account.js" type="text/javascript"></script>-->
</body>

</html>
