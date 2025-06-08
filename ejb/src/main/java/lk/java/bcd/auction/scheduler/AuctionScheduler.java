package lk.java.bcd.auction.scheduler;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton; // Using Singleton for a scheduler is common, can also be Stateless
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lk.java.bcd.auction.entity.AuctionItem;
import lk.java.bcd.auction.entity.AuctionStatus;
import lk.java.bcd.auction.entity.Bid;
import lk.java.bcd.auction.session.AuctionService; // May not be needed if direct EM access is preferred

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton // Ensures a single instance, good for schedulers managing shared resources/tasks
@Startup   // Ensures the bean is initialized on application startup
public class AuctionScheduler {

    private static final Logger LOGGER = Logger.getLogger(AuctionScheduler.class.getName());

    @PersistenceContext(unitName = "auctionPU")
    private EntityManager em;

    // Optional: Inject AuctionService if complex business logic for finding items is preferred
    // @EJB
    // private AuctionService auctionService;

    /**
     * Periodically checks for auctions that have ended and processes them.
     * Runs every minute. persistent=false means the timer is not saved across server restarts if not desired.
     */
    @Schedule(hour = "*", minute = "*/1", second = "0", persistent = false)
    public void checkAndCloseAuctions() {
        LOGGER.log(Level.INFO, "Scheduler running: Checking for auctions to close at {0}", new Date());

        try {
            TypedQuery<AuctionItem> query = em.createQuery(
                "SELECT ai FROM AuctionItem ai WHERE ai.endTime <= :now AND ai.status = :openStatus",
                AuctionItem.class
            );
            query.setParameter("now", new Date());
            query.setParameter("openStatus", AuctionStatus.OPEN);

            List<AuctionItem> itemsToClose = query.getResultList();

            if (itemsToClose.isEmpty()) {
                LOGGER.log(Level.INFO, "No auctions to close at this time.");
                return;
            }

            for (AuctionItem item : itemsToClose) {
                try {
                    // Use pessimistic lock to prevent concurrent modifications if this was a highly concurrent app
                    // em.lock(item, LockModeType.PESSIMISTIC_WRITE);

                    LOGGER.log(Level.INFO, "Processing auction item ID: {0}, Name: {1}", new Object[]{item.getId(), item.getName()});

                    if (item.getBids() == null || item.getBids().isEmpty()) {
                        item.setStatus(AuctionStatus.CLOSED_NO_BIDS);
                        LOGGER.log(Level.INFO, "Auction ID {0} ('{1}') closed with no bids.", new Object[]{item.getId(), item.getName()});
                    } else {
                        // Find the highest bid
                        Optional<Bid> winningBidOpt = item.getBids().stream()
                                .max(Comparator.comparing(Bid::getBidAmount) // Primary sort by amount
                                             .thenComparing(Bid::getBidTime)); // Secondary sort by earliest time for ties

                        if (winningBidOpt.isPresent()) {
                            Bid winningBid = winningBidOpt.get();
                            item.setWinningUser(winningBid.getBidder());
                            item.setCurrentPrice(winningBid.getBidAmount()); // Ensure currentPrice reflects final winning bid
                            item.setStatus(AuctionStatus.CLOSED_WITH_WINNER);
                            LOGGER.log(Level.INFO, "Auction ID {0} ('{1}') closed. Winner: {2} with bid {3}",
                                       new Object[]{item.getId(), item.getName(), winningBid.getBidder().getUsername(), winningBid.getBidAmount()});
                        } else {
                            // Should not happen if bids list is not empty, but as a safeguard
                            item.setStatus(AuctionStatus.CLOSED_NO_BIDS);
                            LOGGER.log(Level.WARNING, "Auction ID {0} ('{1}') had bids collection but no max bid found. Closed with no bids.", new Object[]{item.getId(), item.getName()});
                        }
                    }
                    em.merge(item);
                } catch (Exception e) {
                    // Log and continue with the next item, don't let one failed auction stop others.
                    LOGGER.log(Level.SEVERE, "Error processing auction item ID: " + (item != null ? item.getId() : "unknown"), e);
                }
            }
            LOGGER.log(Level.INFO, "Finished processing {0} auctions.", itemsToClose.size());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during scheduled auction closing task", e);
        }
    }
}
