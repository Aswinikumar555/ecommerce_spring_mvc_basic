package com.vedantu.requests;

public abstract class AbstractFrontEndListReq extends AbstractFrontEndReq {

    private Integer start;
    private Integer size = 20;//this is size 

    public AbstractFrontEndListReq(Integer start, Integer limit) {
        super();
        this.start = start;
        this.size = limit;
    }


    public AbstractFrontEndListReq() {
        super();
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
    
}
