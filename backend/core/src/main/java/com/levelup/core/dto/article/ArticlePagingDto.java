package com.levelup.core.dto.article;

import java.time.LocalDateTime;

public interface ArticlePagingDto {

    Long getArticleId();
    String getTitle();
    Long getViews();
    String getArticleType();
    LocalDateTime getCreatedAt();
    String getWriter();
    Long getCommentCount();
    Long getVoteCount();
}