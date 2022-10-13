package jjfactory.webclient.business.post.domain;

import jjfactory.webclient.business.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class PostImage{
    private String url;

    public PostImage(String url) {
        this.url = url;
    }
}
