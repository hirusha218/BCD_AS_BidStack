<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Online Auction System</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .auction-item img {
            max-height: 300px;
            object-fit: contain;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 15px;
            text-align: center;
            margin-top: 50px;
        }
    </style>
</head>
<body class="bg-light">

<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="#">BidStack</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">

            <!-- Search Bar -->
            <form class="d-flex ms-auto me-3" role="search">
                <input class="form-control me-2" type="search" placeholder="Search items" aria-label="Search">
                <button class="btn btn-outline-light" type="submit">Search</button>
            </form>

            <!-- More Dropdown -->
            <ul class="navbar-nav me-3">
                <li class="nav-item">
                    <a class="nav-link active" href="viewItem.jsp">Home</a>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="moreDropdown" role="button" data-bs-toggle="dropdown">
                        More
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="moreDropdown">
                        <li><a class="dropdown-item" href="#">My Bids</a></li>
                        <li><a class="dropdown-item" href="#">My Account</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="#">Logout</a></li>
                    </ul>
                </li>
            </ul>

            <!-- Login / Sign Up / Logout -->
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="btn btn-outline-light me-2" href="login.jsp">Login</a>
                </li>
                <li class="nav-item">
                    <a class="btn btn-warning me-2" href="signup.jsp">Sign Up</a>
                </li>

            </ul>

        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header bg-dark text-white">
            <h3 class="mb-0">Live Auction: Smartwatch Pro 3</h3>
            <small class="text-warning">Ends in: <strong>01h 23m 45s</strong></small>
        </div>

        <div class="card-body">
            <div class="row auction-item mb-4">
                <div class="col-md-5">
                    <img src="https://www.bing.com/images/search?view=detailV2&ccid=Cgg7x4If&id=1B4FABDC5CC4E668169771E15B0C3318504B6F4F&thid=OIP.Cgg7x4IfpiNhCdalpA8DOQHaHc&mediaurl=https%3a%2f%2fm.media-amazon.com%2fimages%2fI%2f71AcGKTe9%2bL._AC_SL1500_.jpg&cdnurl=https%3a%2f%2fth.bing.com%2fth%2fid%2fR.0a083bc7821fa6236109d6a5a40f0339%3frik%3dT29LUBgzDFvhcQ%26pid%3dImgRaw%26r%3d0&exph=1500&expw=1491&q=Smartwatch&simid=608055585120806939&FORM=IRPRST&ck=52F5E74DD23749935FFD2A78FAE952CB&selectedIndex=0&itb=0" alt="Smartwatch" class="img-fluid rounded border">
                </div>
                <div class="col-md-7">
                    <h4>Product Description</h4>
                    <p>
                        Premium Smartwatch Pro 3 with heart rate monitoring, GPS, Bluetooth, and 7-day battery life.
                        Perfect for fitness and daily use. Brand new with warranty.
                    </p>
                    <h5 class="text-success mt-4">Current Highest Bid:</h5>
                    <h2 id="highestBid" class="text-primary">Rs. 12,000</h2>

                    <form onsubmit="event.preventDefault(); placeBid();" class="mt-3">
                        <div class="input-group">
                            <span class="input-group-text">Rs.</span>
                            <input type="number" class="form-control" id="bidAmount" placeholder="Enter your bid" required>
                            <button class="btn btn-success" type="submit">Place Bid</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Bid History -->
            <div class="row">
                <div class="col-md-12">
                    <h5 class="mb-3">Bid History</h5>
                    <ul class="list-group" id="bidHistory">
                        <li class="list-group-item">User1 bid Rs. 12,000</li>
                        <li class="list-group-item">User2 bid Rs. 11,500</li>
                        <li class="list-group-item">User3 bid Rs. 11,000</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="footer mt-5">
    <div class="container">
        <p class="text-muted mb-0">&copy; 2025 AuctionZone. All rights reserved.</p>
    </div>
</div>

<!-- JavaScript -->
<script>
    function placeBid() {
        const amount = document.getElementById("bidAmount").value;
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "PlaceBidServlet", true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.getElementById("bidAmount").value = '';
            }
        };
        xhr.send("amount=" + amount);
    }

    function connectToLiveUpdates() {
        const eventSource = new EventSource("BidUpdatesServlet");
        eventSource.onmessage = function (event) {
            const data = JSON.parse(event.data);
            document.getElementById("highestBid").innerText = "Rs. " + data.amount;
            const history = document.getElementById("bidHistory");
            const newBid = document.createElement("li");
            newBid.className = "list-group-item";
            newBid.innerText = data.username + " bid Rs. " + data.amount;
            history.insertBefore(newBid, history.firstChild);
        };
    }

    window.onload = function () {
        connectToLiveUpdates();
    };
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
