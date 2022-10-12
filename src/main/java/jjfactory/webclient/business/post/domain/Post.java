package jjfactory.webclient.business.post.domain;


import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    private String title;
    @Lob
    private String content;

    private BigDecimal price;
    private int viewCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "photo", joinColumns = @JoinColumn(name = "post_id"))
    private List<PostImage> images = new ArrayList<>();

    @Builder
    public Post(Member member, Category category, String title, String content, BigDecimal price, int viewCount, List<PostImage> images) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.price = price;
        this.viewCount = viewCount;
        this.images = images;
    }
}
