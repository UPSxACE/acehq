package com.upsxace.acehq.modules.post.service;

import com.upsxace.acehq.config.error.ForbiddenException;
import com.upsxace.acehq.config.error.NotFoundException;
import com.upsxace.acehq.modules.post.dto.PostDto;
import com.upsxace.acehq.modules.post.dto.PublishPostRequest;
import com.upsxace.acehq.modules.post.entity.Post;
import com.upsxace.acehq.modules.post.entity.PostLike;
import com.upsxace.acehq.modules.post.mapper.PostMapper;
import com.upsxace.acehq.modules.post.repository.PostLikeRepository;
import com.upsxace.acehq.modules.post.repository.PostRepository;
import com.upsxace.acehq.modules.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMapper postMapper;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostDto publish(PublishPostRequest request){
        var profile = userService.getUserProfile().orElseThrow(IllegalStateException::new);
        var post = Post.builder().text(request.getText()).profile(profile).build();
        postRepository.saveAndFlush(post);
        return postMapper.toDto(post);
    }

    public PostDto userGetById(UUID id){
        var userId = userService.getUserId().orElseThrow(IllegalStateException::new);
        var details = postRepository.findByIdAndDeletedAtIsNullWithDetails(id, userId).orElseThrow(NotFoundException::new);
        return postMapper.toDetailedDto(details);
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
    public void userDislikePost(UUID postId){
        var post = postRepository.findByIdAndDeletedAtIsNullForUpdate(postId).orElseThrow(NotFoundException::new);
        var userId = userService.getUserId().orElseThrow(IllegalStateException::new);
        postLikeRepository.findByPostIdAndProfileIdForUpdate(post.getId(), userId)
                .ifPresent(postLike -> {
                    post.setLikesCount(post.getLikesCount() - 1);
                    postRepository.save(post);
                    postLikeRepository.delete(postLike);
                });
    }
}
