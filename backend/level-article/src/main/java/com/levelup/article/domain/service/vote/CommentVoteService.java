package com.levelup.article.domain.service.vote;

import com.levelup.article.domain.entity.ArticleComment;
import com.levelup.article.domain.entity.CommentVote;
import com.levelup.article.domain.repository.CommentRepository;
import com.levelup.article.domain.repository.CommentVoteRepository;
import com.levelup.article.domain.service.dto.VoteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentVoteService implements VoteService {

    private final CommentRepository commentRepository;
    private final CommentVoteRepository commentVoteRepository;

    public VoteDto save(VoteDto dto) {
        if (validateDuplicationAndDelete(dto.getMemberId(), dto.getTargetId())) {
            ArticleComment article = commentRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

            CommentVote vote = dto.toEntity(article);
            commentVoteRepository.save(vote);

            return VoteDto.of(vote, true);
        }

        return VoteDto.of(dto.getMemberId(), dto.getTargetId(), dto.getVoteType(), false);
    }

    public boolean validateDuplicationAndDelete(Long memberId, Long commentId) {
        final List<CommentVote> votes = commentVoteRepository.findByMemberIdAndCommentId(memberId, commentId);

        if (!votes.isEmpty()) {
            CommentVote vote = votes.get(0);
            commentVoteRepository.delete(vote);
            return false;
        }
        return true;
    }
}