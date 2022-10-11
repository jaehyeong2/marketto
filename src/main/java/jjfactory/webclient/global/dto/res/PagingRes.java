package jjfactory.webclient.global.dto.res;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
public class PagingRes<T> {
    private int currentPage;
    private int totalPage;
    private long totalCount;
    private List<T> resultList;

    public PagingRes(int currentPage, int totalPage, long totalCount, List<T> resultList) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.resultList = resultList;
    }

    public PagingRes(Page<T> page) {
        this.currentPage = page.getPageable().getPageNumber()+1;
        this.totalPage = page.getTotalPages();
        this.totalCount = page.getTotalElements();
        this.resultList = page.getContent();
    }
}
