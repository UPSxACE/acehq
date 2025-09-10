package com.upsxace.acehq.modules.post.controller;

import com.upsxace.acehq.modules.post.dto.PostDto;
import com.upsxace.acehq.modules.post.dto.PublishPostRequest;
import com.upsxace.acehq.modules.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostControllerV1 {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> publishPost(
            @RequestBody @Valid PublishPostRequest request,
            UriComponentsBuilder uriBuilder
    ){
        var post = postService.publish(request);

        var uri = uriBuilder
                .path("/v1/posts/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(uri).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(postService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id
    ){
        postService.userDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(
            @PathVariable UUID id
    ){
        postService.userLikePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikePost(
            @PathVariable UUID id
    ){
        postService.userDislikePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts(){
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostDto>> getPopularPosts(){
        return ResponseEntity.ok(postService.getPopular());
    }
}
