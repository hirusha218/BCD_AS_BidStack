package lk.java.bcd.auction.session;

import jakarta.ejb.Local;
import lk.java.bcd.auction.entity.AuctionItem;
import java.util.List;

@Local
public interface AuctionService {

    AuctionItem createAuctionItem(AuctionItem item);

    AuctionItem findAuctionItemById(Long id);

    List<AuctionItem> getAllAuctionItems();


    AuctionItem placeBid(Long itemId, double bidAmount, String username) throws AuctionException, UserNotFoundException, Exception;

    void placeBid(Long itemId, double username, double bidAmount);
}
