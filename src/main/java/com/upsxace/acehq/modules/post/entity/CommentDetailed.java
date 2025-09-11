package com.upsxace.acehq.modules.post.entity;

import lombok.Data;

@Data
public class CommentDetailed {
    private final Comment comment;
    private final boolean liked;

    public CommentDetailed(Comment comment, boolean liked){
        this.comment = comment;
        this.liked = liked;
    }
}
