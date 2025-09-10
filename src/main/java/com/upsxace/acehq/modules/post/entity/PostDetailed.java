package com.upsxace.acehq.modules.post.entity;

import lombok.Data;

@Data
public class PostDetailed {
    private final Post post;
    private final boolean liked;

    public PostDetailed(Post post, boolean liked){
        this.post = post;
        this.liked = liked;
    }
}
