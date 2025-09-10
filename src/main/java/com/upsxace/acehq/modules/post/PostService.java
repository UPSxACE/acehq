package com.upsxace.acehq.modules.post;

import com.upsxace.acehq.config.error.ForbiddenException;
import com.upsxace.acehq.config.error.NotFoundException;
import com.upsxace.acehq.modules.post.dto.PublishPostRequest;
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

    @Transactional
    public PostDto publish(PublishPostRequest request){
        var profile = userService.getUserProfile().orElseThrow(IllegalStateException::new);
        var post = Post.builder().text(request.getText()).profile(profile).build();
        postRepository.saveAndFlush(post);
        return postMapper.toDto(post);
    }

    public PostDto getById(UUID id){
        return postMapper.toDto(postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(NotFoundException::new));
    }

    @Transactional
    public void userDeleteById(UUID id){
        var post = postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(NotFoundException::new);
        var user = userService.getUserContext().orElseThrow(IllegalStateException::new);
        if(!post.getProfile().getId().equals(user.getId()) && !user.isAdmin()) throw new ForbiddenException();
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
