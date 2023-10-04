package com.shopproject.shopbt.request;

import lombok.*;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
public class OffsetBasedPageRequest implements Pageable {
    private int limit;
    private int offset;
    private Sort sort;
    /**
            * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
            * @param offset zero-based offset.
            * @param limit  the size of the elements to be returned.
            * @param sort   can be {@literal null}.
     */
    public OffsetBasedPageRequest(int offset,int limit, Sort sort){
        if(limit<1){
            throw new IllegalArgumentException("limit must not be less than one!");
        }
        if(offset<0){
            throw new IllegalArgumentException("offset index must not be less than");
        }
        this.offset=offset;
        this.limit=limit;
        this.sort=sort;
    }
    /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset     zero-based offset.
     * @param limit      the size of the elements to be returned.
     * @param direction  the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public OffsetBasedPageRequest(int offset,int limit,Sort.Direction direction, String... properties){
        this(offset,limit,Sort.by(direction,properties));
    }
    /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     */
    public OffsetBasedPageRequest(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }
    @Override
    public boolean isPaged() {
        return Pageable.super.isPaged();
    }

    @Override
    public boolean isUnpaged() {
        return Pageable.super.isUnpaged();
    }

    @Override
    public int getPageNumber() {
        return offset/limit;
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
    public Sort getSortOr(Sort sort) {
        return Pageable.super.getSortOr(sort);
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest((int) (getOffset() + getPageSize()),getPageSize(),getSort());
    }
    public OffsetBasedPageRequest previous(){
        return hasPrevious()?new OffsetBasedPageRequest((int) (getOffset()-getPageSize()),getPageSize(),getSort()):this;
    }
    @Override
    public Pageable previousOrFirst() {
        return hasPrevious()?previous():first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0,getPageSize(),getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return offset>limit;
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Pageable.super.toOptional();
    }

    @Override
    public OffsetScrollPosition toScrollPosition() {
        return Pageable.super.toScrollPosition();
    }
}
