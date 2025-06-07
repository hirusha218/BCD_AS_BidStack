package lk.java.bcd.auction.session;

import jakarta.ejb.Local;
import lk.java.bcd.auction.entity.AuctionItem;
import java.util.List;

@Local
public interface AuctionService {

    AuctionItem createAuctionItem(AuctionItem item);

    AuctionItem findAuctionItemById(Long id);

    List<AuctionItem> getAllAuctionItems();

    boolean placeBid(Long itemId, double bidAmount); // Changed to boolean to indicate success/failure
}
