<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    <title>Dashboard - AceBank</title>
</head>
<body>
<header>
    <div class="container header-flex">
        <h1 class="logo-text">Ace<span>Bank</span></h1>
        <nav>
            <ul>
                <li><a href="ChangePassword.jsp">Reset Password</a></li>
                <li><a href="Logout" class="logout-btn">Logout</a></li>
            </ul>
        </nav>
    </div>
</header>

<main class="container">
    <div class="welcome-header">
        <h1>Hello, ${sessionScope.firstName}</h1>
        <p>Account Number: <strong>${sessionScope.accountNumber}</strong></p>
    </div>

    <%-- Success/Error Alerts --%>
    <c:if test="${not empty param.msg}">
        <div class="alert success">${param.msg}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert error">${param.error}</div>
    </c:if>

    <div class="dashboard-grid">
        <%-- Balance Card --%>
        <div class="card balance-card">
            <h3>Total Balance</h3>
            <h1 class="amount">₹ ${sessionScope.balance}</h1>

            <div class="quick-actions">
                <form action="Deposit" method="post" class="action-row">
                    <input type="number" name="amount" placeholder="Amount" step="0.01" required/>
                    <button type="submit" class="btn-deposit">Deposit</button>
                </form>

                <form action="Withdraw" method="post" class="action-row">
                    <input type="number" name="amount" placeholder="Amount" step="0.01" required/>
                    <button type="submit" class="btn-withdraw">Withdraw</button>
                </form>
            </div>
        </div>

        <%-- Transfer Card --%>
        <div class="card transfer-card">
            <h3>Transfer Funds</h3>
            <form action="Transfer" method="post" class="vertical-form">
                <input type="text" name="toAccount" placeholder="Recipient Account No" required/>
                <input type="number" name="toAmount" placeholder="Amount (₹)" step="0.01" required/>
                <button type="submit" class="btn-transfer">Send Money</button>
            </form>
        </div>
    </div>

    <%-- Transactions Table --%>
    <section class="card table-card">
        <h3>Recent Transactions</h3>
        <table class="transaction-table">
            <thead>
            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Info</th>
                <th>Amount</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="tx" items="${sessionScope.transactionDetailsList}">
                <tr>
                    <td>${tx.createdAt()}</td>
                    <td><span class="type-badge">${tx.txType()}</span></td>
                    <td>
                        <c:choose>
                            <c:when test="${tx.txType() == 'TRANSFER'}">
                                ${tx.senderAccount() == sessionScope.accountNumber ? 'To: ' : 'From: '}
                                ${tx.senderAccount() == sessionScope.accountNumber ? tx.receiverAccount() : tx.senderAccount()}
                            </c:when>
                            <c:otherwise>${tx.remark()}</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="${tx.senderAccount() == sessionScope.accountNumber ? 'neg' : 'pos'}">
                            ${tx.senderAccount() == sessionScope.accountNumber ? '-' : '+'} ₹${tx.amount()}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${empty sessionScope.transactionDetailsList}">
            <p class="empty-state">No transactions found.</p>
        </c:if>
    </section>
</main>
</body>
</html>