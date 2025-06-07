<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.java.bcd.auction.entity.AuctionItem" %>
<%@ page import="lk.java.bcd.auction.entity.Bid" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    AuctionItem item = (AuctionItem) request.getAttribute("auctionItem");
    String loggedInUsername = (String) request.getAttribute("loggedInUsername");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
%>
<html>
<head>
    <title>View Item: <%= item != null ? item.getName() : "Item Not Found" %></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <jsp:include page="/WEB-INF/header.jspf" />

    <h1>Auction Item Details</h1>

    <% if (item != null) { %>
        <h2><%= item.getName() %></h2>
        <p><strong>Description:</strong> <%= item.getDescription() %></p>
        <p><strong>Starting Price:</strong> $<%= String.format("%.2f", item.getStartingPrice()) %></p>
        <p><strong>Current Price:</strong> $<%= String.format("%.2f", item.getCurrentPrice()) %></p>
        <p><strong>End Time:</strong> <%= sdf.format(item.getEndTime()) %></p>
        <p><strong>Status:</strong> <%= item.getStatus() %></p>
        <% if (item.getStatus() == lk.java.bcd.auction.entity.AuctionStatus.CLOSED_WITH_WINNER && item.getWinningUser() != null) { %>
            <p><strong>Winner:</strong> <%= item.getWinningUser().getUsername() %> at $<%= String.format("%.2f", item.getCurrentPrice()) %></p>
        <% } else if (item.getStatus() == lk.java.bcd.auction.entity.AuctionStatus.CLOSED_NO_BIDS) { %>
            <p><strong>This auction closed with no bids.</strong></p>
        <% } %>


        <h3>Bids</h3>
        <% List<Bid> bids = item.getBids();
           if (bids == null || bids.isEmpty()) { %>
            <p>No bids placed yet.</p>
        <% } else { %>
            <ul>
                <% for (Bid bid : bids) { %>
                    <li>$<%= String.format("%.2f", bid.getBidAmount()) %> by <%= bid.getBidder().getUsername() %> at <%= sdf.format(bid.getBidTime()) %></li>
                <% } %>
            </ul>
        <% } %>

        <%-- Bidding Form --%>
        <% if (item.getStatus() == lk.java.bcd.auction.entity.AuctionStatus.OPEN && loggedInUsername != null) { %>
            <h3>Place Your Bid</h3>
            <% if (errorMessage != null) { %><p style="color:red;"><%= errorMessage %></p><% } %>
            <% if (successMessage != null) { %><p style="color:green;"><%= successMessage %></p><% } %>
            <form method="post" action="${pageContext.request.contextPath}/auction">
                <input type="hidden" name="action" value="placeBid">
                <input type="hidden" name="itemId" value="<%= item.getId() %>">
                Bid Amount: <input type="number" step="0.01" name="bidAmount" required>
                <input type="submit" value="Place Bid">
            </form>
        <% } else if (item.getStatus() == lk.java.bcd.auction.entity.AuctionStatus.OPEN && loggedInUsername == null) { %>
            <p><a href="${pageContext.request.contextPath}/auction?action=login&redirect=${pageContext.request.contextPath}/auction?action=viewItem%26itemId=<%=item.getId()%>">Login to place a bid.</a></p>
        <% } else if (item.getStatus() != lk.java.bcd.auction.entity.AuctionStatus.OPEN) { %>
            <p>This auction is closed for bidding.</p>
        <% } %>

    <% } else { %>
        <p>Auction item not found.</p>
    <% } %>

    <p><a href="${pageContext.request.contextPath}/auction">Back to Auction List</a></p>

</body>
</html>
