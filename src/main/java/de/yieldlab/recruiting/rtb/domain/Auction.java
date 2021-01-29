package de.yieldlab.recruiting.rtb.domain;

import com.google.common.base.Preconditions;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An auction, with business logic to gather all bid responses and determine the winner.
 */
public class Auction {

    private final UUID id;
    private String reference;
    private Map<String, String> attributes;

    private enum State {
        NOT_STARTED, IN_PROGRESS, DONE
    }

    private List<URI> bidders;

    private Bid winner;

    private List<Bid> bids;

    private State auctionState = State.NOT_STARTED;

    private Object monitor = new Object();

    public Auction(String reference, Map<String, String> attributes, List<URI> bidders) {
        Objects.requireNonNull(bidders, "Need at least one bidder");
        Preconditions.checkArgument(bidders.size() > 0, "Need at least one bidder");

        this.attributes = attributes;
        this.id = UUID.randomUUID();
        this.reference = reference;
        this.bidders = bidders;
    }

    /**
     * Prepare the bid request and gather responses from all bidders. Determine the highest bid and mark it as winner.
     */
    private void doPerform() {
        auctionState = State.IN_PROGRESS;

        BidRequestXO request = new BidRequestXO(reference, attributes);

        RestTemplate restTemplate = new RestTemplate();

        bids = bidders.stream()
                .map((url) -> restTemplate.postForObject(url, request, BidResponseXO.class))
                .map((response) -> new Bid(request, response))
                .sorted().collect(Collectors.toList());


        if (bids.isEmpty()) {
            this.winner = Bid.EMPTY;
        } else {
            this.winner = bids.get(0);
        }

        auctionState = State.DONE;
    }

    public Bid winner() {
        performAndWaitForAuction();
        return winner;
    }

    private void performAndWaitForAuction() {
        if (auctionState != State.DONE) {
            synchronized (monitor) {
                if (auctionState != State.DONE) {
                    doPerform();
                }
            }
        }
    }

    public List<Bid> bids() {
        performAndWaitForAuction();
        return bids;
    }
}
