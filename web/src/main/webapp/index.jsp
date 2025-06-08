<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HiBid Auction</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>
    <div class="top-bar">
        <div class="account-links">
            <a href="login.jsp">Sign In</a>
            <a href="#">Find Auctions</a>
            <a href="#">Blog</a>
            <a href="#">Sell</a>
            <a href="#">Join Our Mailing List</a>
            <a href="#">Manage Notifications</a>
            <select>
                <option>English</option>
            </select>
        </div>
    </div>
    <div class="main-header">
        <div class="logo">
            <img src="https://hibid.com/image.axd?picture=/hibid_logo.png" alt="HiBid Logo">
        </div>
        <div class="search-bar">
            <select>
                <option>Shop by Category</option>
            </select>
            <input type="text" placeholder="Search all of HiBid">
            <input type="text" placeholder="Zip">
            <select>
                <option>50 Miles</option>
            </select>
            <button>Search</button>
        </div>
    </div>
    <nav>
        <a href="#">All Categories</a>
        <a href="#">Today's Events</a>
        <a href="#">Auctions Near Me</a>
        <a href="#">Auctions by State</a>
        <a href="#">Company Search</a>
        <a href="#">Watch List</a>
        <a href="#">Bids</a>
    </nav>
</header>

<main>
    <section class="hero">
        <h1>LIVE & ONLINE AUCTIONS. SEARCH, JOIN & BID NOW</h1>
        <p>OVER 350 ITEMS AT AUCTION</p>
        <div class="auction-highlights">
            <div class="highlight">
                <img src="https://hibid.com/image.axd?picture=/auction1.jpg" alt="Auction Items">
                <p>JUNE 7TH SALE FEDERAL ONLINE GOV'T SURPLUS FEDERAL SEIZURE</p>
                <p>VAI INTERNATIONAL INCORPORATED</p>
                <p>3325 Lots - Ends 6/7/2025</p>
            </div>
            <div class="highlight">
                <img src="https://hibid.com/image.axd?picture=/coins1.jpg" alt="King Gilbert Coins">
                <p>RETURN OF THE KING GILBERTI SERIES: HOUSE OF GILBERTI</p>
                <p>One of a Kind Coins LLC</p>
                <p>200 Lots - Ends 6/5/2025</p>
            </div>
            <div class="highlight">
                <img src="https://hibid.com/image.axd?picture=/coins2.jpg" alt="Memph Minerva Coin">
                <p>JUNE 4TH - 8TH MEMPH MINERVA COIN AUCTION</p>
                <p>Gold Standard Auctions</p>
                <p>4233 Lots - Ends 6/8/2025</p>
            </div>
        </div>
    </section>

    <section class="categories">
        <h2>FIND AUCTIONS BY CATEGORY</h2>
        <div class="category-grid">
            <div class="category">
                <img src="https://www.bing.com/ck/a?!&&p=bd1d56712b9c03424ed6b1442ec399784809cd501d9b09db7b6795de19e49c9eJmltdHM9MTc0OTE2ODAwMA&ptn=3&ver=2&hsh=4&fclid=3948e512-4c78-6440-0c4d-f07e4df16566&u=a1L2ltYWdlcy9zZWFyY2g_cT1BcnQlMjBBcnQmRk9STT1JUUZSQkEmaWQ9RDY0OTA1RUM5RTlGQzBGNUQyMjZEMzFCN0IzRkY2RUFGQkUzN0Y5Rg&ntb=1" alt="Art">
                <p>Art</p>
            </div>
            <div class="category">
                <img src="https://www.bing.com/ck/a?!&&p=1fb33a12cee9d61ee92cf5753d92318a51b51b587402f20623ef4a9ee0d4a257JmltdHM9MTc0OTE2ODAwMA&ptn=3&ver=2&hsh=4&fclid=3948e512-4c78-6440-0c4d-f07e4df16566&u=a1L2ltYWdlcy9zZWFyY2g_cT1hbnRpcXVlcyslMjYrY29sbGVjdGlibGVzJmlkPTQ5MEE2Q0FBNTZBNjA0QjZGQkY1MDE5MEYyMDBCQ0I2M0VCMTdFNDQmRk9STT1JUUZSQkE&ntb=1" alt="Antiques & Collectibles">
                <p>Antiques & Collectibles</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/coins.jpg" alt="Coins & Currency">
                <p>Coins & Currency</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/jewelry.jpg" alt="Jewelry, Watches & Gemstones">
                <p>Jewelry, Watches & Gemstones</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/fashion.jpg" alt="Fashion">
                <p>Fashion</p>
            </div>
            <div class="category">
                <img src="https://plus.unsplash.com/premium_photo-1709589145461-4797b4e80e9c?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8YmFsbHxlbnwwfHwwfHx8MA%3D%3D" alt="Kid & Baby Essentials">
                <p>Kid & Baby Essentials</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/garden.jpg" alt="Lawn & Garden">
                <p>Lawn & Garden</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/furniture.jpg" alt="Furniture">
                <p>Furniture</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/sports.jpg" alt="Sporting Goods">
                <p>Sporting Goods</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/toys.jpg" alt="Toys">
                <p>Toys</p>
            </div>
            <div class="category">
                <img src="https://hibid.com/image.axd?picture=/electronics.jpg" alt="Computers & Electronics">
                <p>Computers & Electronics</p>
            </div>
        </div>
        <button class="view-all">VIEW ALL</button>
        <section class="auction-grid">
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/picasso.jpg" alt="Rare Pablo Picasso Artwork">
                <p>[Rare] Pablo Picasso Artwork (1963) - Visage No. 202</p>
                <p>Today at 6:30 PM<br>Bid during last 9 seconds.<br>06:17:19</p>
                <p class="status">STARTING SOON</p>
                <p>Buy it now for $10,000</p>
                <button class="bid-btn">BID NOW</button>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/camera.jpg" alt="Canon EOS R5 Mark II">
                <p>Canon EOS R5 Mark II Interchangeable Lens Mirrorless</p>
                <p>Today at 7:58 PM<br>Bid during last 9 seconds.<br>07:45:19</p>
                <p class="status">STARTING SOON</p>
                <p>Buy it now for $11,519</p>
                <button class="bid-btn">BID NOW</button>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/macbook1.jpg" alt="Apple MacBook Air 13-inch">
                <p>Apple - MacBook Air 13-inch Apple M4 chip Built</p>
                <p>Today at 10:58 PM<br>Bid during last 9 seconds.<br>10:45:19</p>
                <p class="status">STARTING SOON</p>
                <p>Buy it now for $6,269</p>
                <button class="bid-btn">BID NOW</button>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/macbook2.jpg" alt="Apple MacBook Air 15-inch">
                <p>Apple MacBook Air 15-inch Apple M4 chip Built</p>
                <p>Tomorrow at 1:53 AM<br>Bid during last 9 seconds.<br>13:40:19</p>
                <p class="status">STARTING SOON</p>
                <p>Buy it now for $5,929</p>
                <button class="bid-btn">BID NOW</button>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/bundle.jpg" alt="The Black Label Bundle">
                <p>The Black Label Bundle</p>
                <p>$23.02<br>Distinctive11<br>00:00:09</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $3,030</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/tonic.jpg" alt="Set of 30 wind Oasis Splash">
                <p>Set of 30 - wind Oasis Splash - Refreshing Glow Tonic - 100ml</p>
                <p>$4.95<br>Silva_Bullet<br>00:00:09</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $1,050</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/watch.jpg" alt="Empress Alice Automatic Watch">
                <p>Empress Alice Automatic Ladies Watch - Mint Dial</p>
                <p>$4.16<br>FaridehRoshan<br>00:00:06</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $805</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/giftcard1.jpg" alt="Panera Bread Gift Card">
                <p>$25 Panera Bread Gift Card + 50 bids</p>
                <p>$0.15<br>Toofoo<br>00:00:10</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $555</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/makeup.jpg" alt="Perricone MD No Makeup Smoothing">
                <p>Perricone - MD No Makeup Smoothing Facial Conformer</p>
                <p>$0.46<br>txcardinal<br>00:00:07</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $329</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/bracelet.jpg" alt="Francis Claire Farah Moissanite">
                <p>Francis Claire Farah Moissanite Bracelet - 0.85 Carat DEF-color</p>
                <p>$0.88<br>RickTheTiger<br>00:00:09</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $1,239</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/giftcard2.jpg" alt="Home Depot Digital Gift Card">
                <p>$10 Home Depot® Digital Gift Card + 20 bids</p>
                <p>$0.99<br>heart0322<br>00:00:04</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $22</p>
            </div>
            <div class="auction-item">
                <img src="https://hibid.com/image.axd?picture=/earrings.jpg" alt="Palmero - Lauma Collection">
                <p>Palmero - Lauma Collection - Brielie Earrings</p>
                <p>$0.99<br>futurebuyer<br>00:00:04</p>
                <button class="bid-btn">BID NOW</button>
                <p>Buy it now for $280</p>
            </div>
        </section>
    </section>
</main>

<footer>
    <a href="#">Feedback / Question</a>
</footer>
</body>
</html>