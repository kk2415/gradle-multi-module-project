package com.together.levelup.dto.comment;

import com.together.levelup.domain.comment.ArticleIdentity;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateReplyCommentRequest {

    @NotNull
    private Long parentId;

    @NotNull
    private Long articleId;

    @NotNull
    private String content;

    @NotNull
    private ArticleIdentity identity;

}