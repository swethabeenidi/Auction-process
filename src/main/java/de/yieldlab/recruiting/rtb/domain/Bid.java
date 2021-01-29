package de.yieldlab.recruiting.rtb.domain;


import com.google.common.base.MoreObjects;

/**
 * A bid object. Contains a bid request and response. Natural order is by bid price from the response.
 */
public class Bid implements Comparable<Bid> {
    public static final Bid EMPTY = new Bid();

    private BidRequestXO request;
    private BidResponseXO response;

    public Bid(BidRequestXO request, BidResponseXO response) {
        this.request = request;
        this.response = response;
    }

    private Bid() {

    }

    public BidResponseXO getResponse() {
        return response;
    }

    public BidRequestXO getRequest() {
        return request;
    }

    @Override
    public int compareTo(Bid o) {
        if (o == null) {
            return 1;
        }

        if (getResponse() == o.getResponse()) { // both null or same object
            return 0;
        }

        if (o.getResponse() == null) {
            return 1;
        } else if (this.getResponse() == null) {
            return -1;
        }

        // no null response to be seen, fall back to integer comparison
        return new Integer(o.getResponse().getBid()).compareTo(getResponse().getBid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("request", request).add("response", response).toString();
    }
}
