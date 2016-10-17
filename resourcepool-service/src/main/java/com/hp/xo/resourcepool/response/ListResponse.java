package com.hp.xo.resourcepool.response;

import java.util.List;

public class ListResponse<T> extends BaseResponse {
    private List<T> responses;
    private Integer count;

    public ListResponse() {
    	super();
    }
    
    public List<T> getResponses() {
        return responses;
    }

    public void setResponses(List<T> responses) {
        this.responses = responses;
    }

    public void setResponses(List<T> responses, Integer count) {
        this.responses = responses;
        this.count = count;
    }
    
    public void setCount(Integer count) {
    	this.count = count;
    }

    public Integer getCount() {
        if (count != null) {
            return count;
        }

        if (responses != null) {
            return responses.size();
        }

        return null;
    }
}
