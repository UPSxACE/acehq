package com.upsxace.acehq.modules.post.mapper;

import com.upsxace.acehq.modules.post.dto.CommentDto;
import com.upsxace.acehq.modules.post.entity.Comment;
import com.upsxace.acehq.modules.post.entity.CommentDetailed;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);

    default CommentDto toDetailedDto(CommentDetailed commentDetailed){
        var dto = toDto(commentDetailed.getComment());
        dto.setLiked(commentDetailed.isLiked());
        return dto;
    }
}
