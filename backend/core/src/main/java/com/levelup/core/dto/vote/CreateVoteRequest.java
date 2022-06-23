package com.levelup.core.dto.vote;

import com.levelup.core.domain.Article.ArticleType;
import com.levelup.core.domain.vote.VoteType;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class CreateVoteRequest {

    @NotNull
    private Long ownerId;

    @NotNull
    private VoteType voteType;

}
