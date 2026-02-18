<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/remixicon/3.5.0/remixicon.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <title>Home</title>
</head>
<body class="${cookie.theme_pref.value}">

<header>
    <div class="container header-flex">
        <h1 class="logo-text">Ace<span>Bank</span></h1>
        <nav>
            <ul class="nav-list">
                <li>
                    <div class="theme-switch-wrapper">
                        <label class="theme-switch" for="theme-checkbox">
                            <input type="checkbox"
                                   id="theme-checkbox" ${cookie.theme_pref.value == 'dark' ? 'checked' : ''} />
                            <div class="slider round">
                                <i class="ri-sun-line icon-sun"></i>
                                <i class="ri-moon-line icon-moon"></i>
                            </div>
                        </label>
                    </div>
                </li>
                <%--                <li><a href="loans.jsp"><i class="ri-bank-card-line"></i> Apply Loan</a></li>--%>
                <li><a href="ChangePassword.jsp">Reset Password</a></li>
                <li><a href="Logout" class="logout-btn"><i class="ri-logout-box-r-line"></i> Logout</a></li>
            </ul>
        </nav>
    </div>
</header>

<main class="container fade-in-up">
    <div class="welcome-header">
        <h1>Hello, ${sessionScope.firstName}</h1>
        <p>Account: <span class="acc-badge">${sessionScope.accountNumber}</span></p>
    </div>

    <%-- Alerts --%>
    <c:if test="${not empty param.msg}">
        <div class="alert success">${param.msg}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert error">${param.error}</div>
    </c:if>

    <div class="dashboard-grid">
        <%-- 1. Balance Card (Overview Only) --%>
        <div class="card balance-card">
            <div class="card-header">
                <h3>Total Balance</h3>
                <i class="ri-wallet-3-line"></i>
            </div>
            <h1 class="amount">₹ <span id="balance-counter">${sessionScope.balance}</span></h1>
            <p class="text-muted">Available for withdrawal</p>
        </div>

        <%-- 2. New Deposit Card (Dedicated Section) --%>
        <div class="card deposit-card">
            <div class="card-header">
                <h3>Quick Deposit</h3>
                <i class="ri-add-circle-line"></i>
            </div>
            <form action="home" method="post" class="vertical-form">
                <div class="input-group">
                    <input type="text" name="deposit" placeholder="Amount to Add (₹)"
                           pattern="[0-9]*\.?[0-9]+" inputmode="decimal" required/>
                </div>
                <button type="submit" class="btn-deposit full-width">Add to Balance</button>
            </form>
        </div>

        <%-- 3. Transfer Card --%>
        <div class="card transfer-card">
            <div class="card-header">
                <h3>Send Money</h3>
                <i class="ri-send-plane-fill"></i>
            </div>
            <form action="home" method="post" class="vertical-form">
                <input type="text" name="toAccount" placeholder="Recipient Account No" required/>
                <input type="text" name="toAmount" placeholder="Amount (₹)"
                       pattern="[0-9]*\.?[0-9]+" inputmode="decimal" required/>
                <button type="submit" class="btn-transfer full-width">Transfer Now</button>
            </form>
        </div>

        <%-- 4. Withdraw Card --%>
        <div class="card withdraw-card">
            <div class="card-header">
                <h3>Withdraw Money</h3>
                <i class="ri-bank-card-line"></i>
            </div>
            <form action="home" method="post" class="vertical-form">
                <div class="input-group">
                    <input type="text" name="withdraw" placeholder="Amount to Withdraw (₹)"
                           pattern="[0-9]*\.?[0-9]+" inputmode="decimal" required/>
                </div>
                <button type="submit" class="btn-withdraw full-width">Withdraw Now</button>
            </form>
        </div>
    </div>

    <%-- Transactions Table --%>
    <section class="card table-card">
        <div class="card-header table-header">
            <h3>Recent Transactions</h3>
            <button class="btn-secondary" onclick="downloadStatement()">
                <i class="ri-download-2-line"></i> Download CSV
            </button>
        </div>
        <table class="transaction-table">
            <thead>
            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Reference</th>
                <th>Amount</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="tx" items="${sessionScope.transactionDetailsList}">
                <tr>
                    <td class="text-muted">${tx.createdAt()}</td>
                    <td><span class="type-badge ${tx.txType().toLowerCase()}">${tx.txType()}</span></td>
                    <td>
                        <c:choose>
                            <c:when test="${tx.txType() == 'TRANSFER'}">
                                <span class="ref-text">${tx.senderAccount() == sessionScope.accountNumber ? 'To' : 'From'} ${tx.senderAccount() == sessionScope.accountNumber ? tx.receiverAccount() : tx.senderAccount()}</span>
                            </c:when>
                            <c:otherwise>${tx.remark()}</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="${tx.senderAccount() == sessionScope.accountNumber ? 'neg' : 'pos'} font-bold">
                            ${tx.senderAccount() == sessionScope.accountNumber ? '-' : '+'} ₹${tx.amount()}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/countup.js/1.9.3/countUp.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/dark-mode.js"></script>
<script>
    // 1. Balance Counter Animation
    window.onload = function () {
        const balance = parseFloat("${sessionScope.balance}");
        const options = {decimalPlaces: 2, duration: 2, useEasing: true, prefix: ''};
        const demo = new CountUp('balance-counter', 0, balance, 2, 2.5, options);
        if (!demo.error) {
            demo.start();
        } else {
            console.error(demo.error);
        }
    };

    function downloadStatement() {
        alert("Preparing your statement for download...");
        // Future Download Feature
    }

</script>

<script src="${pageContext.request.contextPath}/assets/js/dark-mode.js"></script>

</body>
</html>