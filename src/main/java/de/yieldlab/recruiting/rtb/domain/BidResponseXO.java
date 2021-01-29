package de.yieldlab.recruiting.rtb.domain;

import com.google.common.base.MoreObjects;

public class BidResponseXO {

    private String id;
    private int bid;
    private String content;

    public BidResponseXO() {
    }

    public BidResponseXO(String id, Integer bid, String content) {
        this.id = id;
        this.bid = bid;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("bid", bid).add("content", content).toString();
    }
}
