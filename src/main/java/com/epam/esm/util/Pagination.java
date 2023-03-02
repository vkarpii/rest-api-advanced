package com.epam.esm.util;

import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.exception.PaginationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNullElse;

@Data
@Slf4j
public class Pagination {

    private Integer page;

    private Integer size;

    public Pagination(Integer page, Integer size) {
        this.page = requireNonNullElse(page,1);
        this.size = requireNonNullElse(size,10);
        if (this.page <= 0){
            throw new PaginationException(ExceptionMessage.WRONG_PAGE);
        }
        if (this.size <= 0){
            throw new PaginationException(ExceptionMessage.WRONG_SIZE);
        }
    }

    public Integer getOffsetPosition(){
        return (page - 1) * getSize();
    }

}
