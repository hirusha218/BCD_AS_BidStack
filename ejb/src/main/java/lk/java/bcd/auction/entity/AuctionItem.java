package lk.java.bcd.auction.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class AuctionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "winning_user_id")
    private User winningUser;

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionStatus status;

    // Constructors
    public AuctionItem() {
        this.status = AuctionStatus.OPEN; // Default status
    }

    public AuctionItem(String name, String description, double startingPrice, Date endTime) {
        this.name = name;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice; // Initial current price is the starting price
        this.endTime = endTime;
        this.status = AuctionStatus.OPEN; // Default status
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getWinningUser() {
        return winningUser;
    }

    public void setWinningUser(User winningUser) {
        this.winningUser = winningUser;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    // Helper method to add a bid
    public void addBid(Bid bid) {
        bids.add(bid);
        bid.setAuctionItem(this);
    }

    public void removeBid(Bid bid) {
        bids.remove(bid);
        bid.setAuctionItem(null);
    }

    @Override
    public String toString() {
        return "AuctionItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startingPrice=" + startingPrice +
                ", currentPrice=" + currentPrice +
                ", endTime=" + endTime +
                ", winningUser=" + (winningUser != null ? winningUser.getUsername() : "null") +
                ", status=" + status +
                ", bidsCount=" + (bids != null ? bids.size() : 0) +
                '}';
    }
}
