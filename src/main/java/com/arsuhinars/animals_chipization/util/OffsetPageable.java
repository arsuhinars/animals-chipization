package com.arsuhinars.animals_chipization.util;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPageable implements Pageable {

    private final int limit;
    private final int offset;

    private final Sort sort;

    public OffsetPageable(int limit, int offset) {
        this(limit, offset, Sort.by("id"));
    }

    public OffsetPageable(int limit, int offset, Sort sort) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
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
    public @NonNull Sort getSort() {
        return sort;
    }

    @Override
    public @NonNull Pageable next() {
        return new OffsetPageable(getPageSize(), (int)(getOffset() + getPageSize()));
    }

    @Override
    public @NonNull Pageable previousOrFirst() {
        return hasPrevious() ?
            new OffsetPageable(getPageSize(), (int)(getOffset() - getPageSize())) : first();
    }

    @Override
    public @NonNull Pageable first() {
        return new OffsetPageable(getPageSize(), 0);
    }

    @Override
    public @NonNull Pageable withPage(int pageNumber) {
        return new OffsetPageable(getPageSize(), pageNumber * getPageSize());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
