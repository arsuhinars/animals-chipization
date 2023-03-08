package com.arsuhinars.animals_chipization.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPageable implements Pageable {

    private int limit;
    private int offset;

    private Sort sort = Sort.by("id");

    public OffsetPageable(int limit, int offset) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageable(getPageSize(), (int)(getOffset() + getPageSize()));
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ?
            new OffsetPageable(getPageSize(), (int)(getOffset() - getPageSize())) : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageable(getPageSize(), 0);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPageable(getPageSize(), pageNumber * getPageSize());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
