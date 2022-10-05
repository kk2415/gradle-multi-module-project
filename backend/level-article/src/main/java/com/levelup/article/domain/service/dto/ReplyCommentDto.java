package com.levelup.article.domain.service.dto;

import com.levelup.article.domain.entity.Article;
import com.levelup.article.domain.ArticleType;
import com.levelup.article.domain.entity.ArticleComment;
import com.levelup.member.domain.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
public class ReplyCommentDto extends CommentDto {

    private Long parentId;

    public ReplyCommentDto(Long commentId,
                           Long memberId,
                           Long parentId,
                           String writer,
                           String content,
                           LocalDateTime createdAt,
                           Long voteCount,
                           Long replyCount,
                           Long articleId,
                           ArticleType articleType) {
        super(commentId, memberId, writer, content, createdAt, voteCount, replyCount, articleId, articleType);
        this.parentId = parentId;
    }

    public static ReplyCommentDto from(ArticleComment comment) {
        return new ReplyCommentDto(
            comment.getId(),
            comment.getMember().getId(),
            comment.getParent().getId(),
            comment.getMember().getNickname(),
            comment.getContent(),
            comment.getCreatedAt(),
            (long) comment.getCommentVotes().size(),
            (long) comment.getChild().size(),
            comment.getArticle().getId(),
            comment.getArticle().getArticleType()
        );
    }

    public static ReplyCommentDto of(Long articleId, Long parentId, String content, ArticleType articleType) {
        return new ReplyCommentDto(
                null,
                null,
                parentId,
                null,
                content,
                null,
                null,
                null,
                articleId,
                articleType);
    }

    public ArticleComment toEntity(Member member, Article article) {
        ArticleComment comment = ArticleComment.builder()
                .member(member)
                .content(content)
                .child(new ArrayList<>())
                .commentVotes(new ArrayList<>())
                .build();

        comment.setArticle(article);
        return comment;
    }
}