package jjfactory.webclient.global.dto.res;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiPageRes<T> {
    private PagingRes<T> result;
}
