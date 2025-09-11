package com.upsxace.acehq.modules.post.service;

import com.upsxace.acehq.config.error.ForbiddenException;
import com.upsxace.acehq.config.error.NotFoundException;
import com.upsxace.acehq.modules.post.dto.CommentDto;
import com.upsxace.acehq.modules.post.dto.CommentRequest;
import com.upsxace.acehq.modules.post.dto.PostDto;
import com.upsxace.acehq.modules.post.dto.PublishPostRequest;
import com.upsxace.acehq.modules.post.entity.Comment;
import com.upsxace.acehq.modules.post.entity.Post;
import com.upsxace.acehq.modules.post.entity.PostLike;
import com.upsxace.acehq.modules.post.mapper.CommentMapper;
import com.upsxace.acehq.modules.post.mapper.PostMapper;
import com.upsxace.acehq.modules.post.repository.CommentRepository;
import com.upsxace.acehq.modules.post.repository.PostLikeRepository;
import com.upsxace.acehq.modules.post.repository.PostRepository;
import com.upsxace.acehq.modules.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMapper postMapper;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public PostDto publish(PublishPostRequest request){
        var profile = userService.getUserProfile().orElseThrow(IllegalStateException::new);
        var post = Post.builder().text(request.getText()).profile(profile).build();
        postRepository.saveAndFlush(post);
        return postMapper.toDto(post);
    }

    public PostDto getById(UUID id){
        var userId = userService.getUserId().orElse(null);
        if(userId == null) {
            var post = postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(NotFoundException::new);
            return postMapper.toDto(post);
        }
        var postDetailed = postRepository.findByIdAndProfileIdAndDeletedAtIsNullWithDetails(id, userId).orElseThrow(NotFoundException::new);
        return postMapper.toDetailedDto(postDetailed);
    }

    @Transactional
    public void userDeleteById(UUID postId){
        var post = postRepository.findByIdAndDeletedAtIsNull(postId).orElseThrow(NotFoundException::new);
        var user = userService.getUserContext().orElseThrow(IllegalStateException::new);
        if(!post.getProfile().getId().equals(user.getId()) && !user.isAdmin()) throw new ForbiddenException();
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    public void userLikePost(UUID postId){
        var post = postRepository.findByIdAndDeletedAtIsNullForUpdate(postId).orElseThrow(NotFoundException::new);
        var userId = userService.getUserId().orElseThrow(IllegalStateException::new);
        var postLike = postLikeRepository.findByPostIdAndProfileIdForUpdate(post.getId(), userId);
        if(postLike.isEmpty()){
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
            postLikeRepository.save(PostLike.builder().postId(post.getId()).profileId(userId).build());
        }
    }

    @Transactional
    public void userUnlikePost(UUID postId){
        var post = postRepository.findByIdAndDeletedAtIsNullForUpdate(postId).orElseThrow(NotFoundException::new);
        var userId = userService.getUserId().orElseThrow(IllegalStateException::new);
        postLikeRepository.findByPostIdAndProfileIdForUpdate(post.getId(), userId)
                .ifPresent(postLike -> {
                    post.setLikesCount(post.getLikesCount() - 1);
                    postRepository.save(post);
                    postLikeRepository.delete(postLike);
                });
    }

    public List<PostDto> getAll(){
        var userId = userService.getUserId().orElse(null);
        if(userId == null){
            return postMapper.toDtos(postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc());
        }
        return postMapper.toDetailedDtos(postRepository.findAllByProfileIdAndDeletedAtIsNullOrderByCreatedAtDescWithDetails(userId));
    }

    public List<PostDto> getPopular(){
        var userId = userService.getUserId().orElse(null);
        var weekAgo = LocalDateTime.now().minus(Duration.ofDays(7));
        if(userId == null){
            return postMapper.toDtos(postRepository.findPopularPostsAfterDate(weekAgo));
        }
        return postMapper.toDetailedDtos(postRepository.findPopularPostsAfterDateWithDetails(userId, weekAgo));
    }

    @Transactional
    public CommentDto userComment(CommentRequest request, UUID postId){
        var post = postRepository.findByIdAndDeletedAtIsNullForUpdate(postId).orElseThrow(NotFoundException::new);
        var profile = userService.getUserProfile().orElseThrow(IllegalStateException::new);
        var comment = Comment.builder().text(request.getText()).profile(profile).post(post).build();
        commentRepository.save(comment);
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.saveAndFlush(post);
        return commentMapper.toDto(comment);
    }

    public CommentDto getCommentById(UUID postId, UUID commentId){
        var userId = userService.getUserId().orElse(null);
        if(userId == null) {
            var comment = commentRepository.findByPostIdAndIdAndPostDeletedAtIsNullAndDeletedAtIsNull(postId, commentId).orElseThrow(NotFoundException::new);
            return commentMapper.toDto(comment);
        }
        var commentDetailed = commentRepository.findByPostIdAndIdAndProfileIdAndPostDeletedAtIsNullAndDeletedAtIsNullWithDetails(postId, commentId, userId).orElseThrow(NotFoundException::new);
        return commentMapper.toDetailedDto(commentDetailed);
    }
}
