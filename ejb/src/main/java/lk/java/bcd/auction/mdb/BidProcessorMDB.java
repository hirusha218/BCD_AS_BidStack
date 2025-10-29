package lk.java.bcd.auction.mdb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import lk.java.bcd.auction.session.AuctionService;

import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/BidQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class BidProcessorMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(BidProcessorMDB.class.getName());

    @EJB
    private AuctionService auctionService; // Injected for future use

    public BidProcessorMDB() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                LOGGER.log(Level.INFO, "Received bid message: {0}", text);

                // Example of how you might parse the message and use the auctionService
                // This is just a placeholder and would need proper parsing and error handling
                // e.g., "itemId=1;bidAmount=150.00"
                /*
                String[] parts = text.split(";");
                if (parts.length == 2) {
                    String itemIdStr = parts[0].substring(parts[0].indexOf("=") + 1);
                    String bidAmountStr = parts[1].substring(parts[1].indexOf("=") + 1);
                    try {
                        Long itemId = Long.parseLong(itemIdStr);
                        double bidAmount = Double.parseDouble(bidAmountStr);

                        LOGGER.log(Level.INFO, "Processing bid for item {0} with amount {1}", new Object[]{itemId, bidAmount});
                        boolean success = auctionService.placeBid(itemId, bidAmount);
                        if (success) {
                            LOGGER.log(Level.INFO, "Bid placed successfully for item {0}", itemId);
                        } else {
                            LOGGER.log(Level.WARNING, "Failed to place bid for item {0}", itemId);
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "Could not parse bid message content: " + text, e);
                    }
                } else {
                    LOGGER.log(Level.WARNING, "Invalid message format received: {0}", text);
                }
                */

            } else {
                LOGGER.log(Level.WARNING, "Received message of unexpected type: {0}", message.getClass().getName());
            }
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Error processing JMS message", e);
            // Consider further error handling, e.g., marking message for redelivery if appropriate
            // MDBContext might be used here if more control over transaction/redelivery is needed
        }
    }
}
