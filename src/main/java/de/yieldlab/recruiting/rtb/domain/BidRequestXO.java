package de.yieldlab.recruiting.rtb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;

import java.util.Map;

public class BidRequestXO {

    private String id;

    private Map<String, String> attributes;

    public BidRequestXO(String id, Map<String, String> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("attributes", attributes).toString();
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
