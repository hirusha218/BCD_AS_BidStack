package lk.java.bcd.auction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double bidAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date bidTime;

    @ManyToOne(optional = false) // A bid must be for an item
    @JoinColumn(name = "item_id", nullable = false)
    private AuctionItem auctionItem;

    @ManyToOne(optional = false) // A bid must have a bidder
    @JoinColumn(name = "user_id", nullable = false)
    private User bidder;

    // Constructors
    public Bid() {
        this.bidTime = new Date();
    }

    public Bid(AuctionItem auctionItem, User bidder, double bidAmount) {
        this.auctionItem = auctionItem;
        this.bidder = bidder;
        this.bidAmount = bidAmount;
        this.bidTime = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                ", auctionItem=" + (auctionItem != null ? auctionItem.getId() : "null") +
                ", bidder=" + (bidder != null ? bidder.getUsername() : "null") +
                '}';
    }
}
