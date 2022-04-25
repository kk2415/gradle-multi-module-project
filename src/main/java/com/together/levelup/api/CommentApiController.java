package com.together.levelup.api;

import com.together.levelup.domain.Comment;
import com.together.levelup.dto.CommentResponse;
import com.together.levelup.dto.CreateCommentRequest;
import com.together.levelup.dto.Result;
import com.together.levelup.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentApiController {
    private final CommentService commentService;

    /***
     * 댓글 생성
     */
    @PostMapping("/comment")
    public CommentResponse create(@RequestBody @Validated CreateCommentRequest commentRequest) {
        Long commentId = commentService.comment(commentRequest.getMemberId(), commentRequest.getChannelId(),
                commentRequest.getContent());

        Comment findComment = commentService.findOne(commentId);
        return new CommentResponse(findComment.getWriter(), findComment.getContent(),
                DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(findComment.getDateCreated()),
                findComment.getVoteCount());
    }

    /***
     * 댓글 조회
     */
    @GetMapping("/comment/{postId}")
    public Result findByPostId(@PathVariable Long postId) {
        List<Comment> findComments = commentService.findByPostId(postId);

        List<CommentResponse> comments = findComments.stream().map(c -> new CommentResponse(c.getWriter(), c.getContent(),
                DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(c.getDateCreated()),
                c.getVoteCount())).collect(Collectors.toList());
        return new Result(comments, comments.size());
    }

}