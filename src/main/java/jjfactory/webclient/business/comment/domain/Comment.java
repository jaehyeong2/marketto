package jjfactory.webclient.business.comment.domain;

import jjfactory.webclient.business.BaseEntity;
import jjfactory.webclient.business.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    private Member member;
}
