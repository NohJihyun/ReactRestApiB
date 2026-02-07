package com.nakshi.rohitour.common.paging;

import java.util.List;

public class PageResponse<T> {
    // 응답 구조 통일
    // 프론트 계산 제거
    private final List<T> list;
    private final int totalCount;
    private final int page;
    private final int size;
    private final int totalPage;

    public PageResponse(List<T> list, int totalCount, PageRequest pageRequest) {
        this.list = list;
        this.totalCount = totalCount;
        this.page = pageRequest.getPage();
        this.size = pageRequest.getSize();
        this.totalPage = (int) Math.ceil((double) totalCount / size);
    }

    public List<T> getList() {
        return list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
