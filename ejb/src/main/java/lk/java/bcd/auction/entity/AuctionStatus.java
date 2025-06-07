package lk.java.bcd.auction.entity;

public enum AuctionStatus {
    OPEN,              // Auction is currently active and accepting bids.
    CLOSED_NO_BIDS,    // Auction has ended, and no bids were placed.
    CLOSED_WITH_WINNER // Auction has ended, and there is a winning bid.
}
