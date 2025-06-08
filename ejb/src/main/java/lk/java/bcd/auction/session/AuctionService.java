package lk.java.bcd.auction.session;

import jakarta.ejb.Local;
import lk.java.bcd.auction.entity.AuctionItem;
import java.util.List;

@Local
public interface AuctionService {

    AuctionItem createAuctionItem(AuctionItem item);

    AuctionItem findAuctionItemById(Long id);

    List<AuctionItem> getAllAuctionItems();

    /**
     * Places a bid on an auction item for a given user.
     * @param itemId The ID of the auction item.
     * @param bidAmount The amount of the bid.
     * @param username The username of the bidder.
     * @return The updated AuctionItem.
     * @throws AuctionException if the bid is invalid (e.g., too low, auction ended).
     * @throws UserNotFoundException if the user placing the bid is not found.
     * @throws Exception for other errors (e.g. item not found)
     */
    AuctionItem placeBid(Long itemId, double bidAmount, String username) throws AuctionException, UserNotFoundException, Exception;
}
