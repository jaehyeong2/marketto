package jjfactory.webclient.global.dto.req;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@NoArgsConstructor
@Getter
public class MyPageReq {
    private int page;
    private int size;

    public PageRequest of(){
        return PageRequest.of(page-1,size);
    }

    public MyPageReq(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
