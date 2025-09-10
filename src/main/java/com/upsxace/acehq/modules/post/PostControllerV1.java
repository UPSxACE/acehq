package com.upsxace.acehq.modules.post;

import com.upsxace.acehq.modules.post.dto.PublishPostRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
}
