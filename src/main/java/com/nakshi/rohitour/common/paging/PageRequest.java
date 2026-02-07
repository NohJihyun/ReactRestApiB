package com.nakshi.rohitour.common.paging;

public class PageRequest {
    
    // page/size 검증 
    // offset 계산 책임을 한 곳으로 모음
    private final int page;
    private final int size;

    public PageRequest(int page, int size) {
        this.page = page <= 0 ? 1 : page;
        this.size = size <= 0 ? 10 : size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
