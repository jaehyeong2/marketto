package jjfactory.webclient.business.post.domain;


import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.business.post.dto.req.PostCreate;
import jjfactory.webclient.business.post.dto.req.PostUpdate;
import lombok.*;

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
    private int likeCount;
    private int reportCount;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "post")
    private List<PostImage> images = new ArrayList<>();

    @Builder
    public Post(Member member, Category category, String title, String content, BigDecimal price, int viewCount, int likeCount, int reportCount) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.price = price;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.reportCount = reportCount;
    }

    public static Post create(PostCreate dto, Member member, Category category){
        return Post.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .member(member)
                .category(category)
                .viewCount(0)
                .likeCount(0)
                .reportCount(0)
                .price(BigDecimal.valueOf(dto.getPrice()))
                .build();
    }

    public void update(PostUpdate dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.price = BigDecimal.valueOf(dto.getPrice());
    }

    public void addImage(PostImage image) {
        this.images.add(image);
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }
    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }

    public void increaseReportCount() {
        this.reportCount += 1;
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }
}
