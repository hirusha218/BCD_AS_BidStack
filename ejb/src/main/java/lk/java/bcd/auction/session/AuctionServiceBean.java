package lk.java.bcd.auction.session;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.java.bcd.auction.entity.AuctionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Stateless
public class AuctionServiceBean implements AuctionService {

    // In-memory storage for simplicity. Replace with database interaction.
    private static final Map<Long, AuctionItem> auctionItems = new ConcurrentHashMap<>();
    private static final AtomicLong currentId = new AtomicLong(1);

    @PersistenceContext(unitName = "auctionPU") // Assuming a Persistence Unit named "auctionPU" is configured
    private EntityManager em;


    @Override
    public AuctionItem createAuctionItem(AuctionItem item) {
        if (item.getId() == null) {
            item.setId(currentId.getAndIncrement());
        }
        if (item.getCurrentPrice() == 0 && item.getStartingPrice() > 0) {
            item.setCurrentPrice(item.getStartingPrice());
        }
        // For now, using in-memory map
        // auctionItems.put(item.getId(), item);
        // return item;
        // Using EntityManager
        em.persist(item);
        return item;
    }

    @Override
    public AuctionItem findAuctionItemById(Long id) {
        // For now, using in-memory map
        // return auctionItems.get(id);
        // Using EntityManager
        return em.find(AuctionItem.class, id);
    }

    @Override
    public List<AuctionItem> getAllAuctionItems() {
        // For now, using in-memory map
        // return new ArrayList<>(auctionItems.values());
        // Using EntityManager
        return em.createQuery("SELECT ai FROM AuctionItem ai", AuctionItem.class).getResultList();
    }

    @Override
    public boolean placeBid(Long itemId, double bidAmount) {
        AuctionItem item = findAuctionItemById(itemId);
        if (item != null) {
            if (bidAmount > item.getCurrentPrice()) {
                // Check if auction is still active (endTime has not passed)
                if (item.getEndTime() != null && item.getEndTime().after(new Date())) {
                    item.setCurrentPrice(bidAmount);
                    // For in-memory
                    // auctionItems.put(itemId, item);
                    // Using EntityManager
                    em.merge(item);
                    return true;
                } else {
                    // Auction has ended
                    System.err.println("Auction for item " + itemId + " has ended.");
                    return false;
                }
            } else {
                // Bid amount is not higher than current price
                 System.err.println("Bid amount " + bidAmount + " is not higher than current price " + item.getCurrentPrice() + " for item " + itemId);
                return false;
            }
        }
        // Item not found
        System.err.println("Item with ID " + itemId + " not found for placing bid.");
        return false;
    }
}
