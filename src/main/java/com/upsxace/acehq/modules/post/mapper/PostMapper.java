package com.upsxace.acehq.modules.post.mapper;

import com.upsxace.acehq.modules.post.dto.PostDto;
import com.upsxace.acehq.modules.post.entity.Post;
import com.upsxace.acehq.modules.post.entity.PostDetailed;
import com.upsxace.acehq.modules.profile.mapper.ProfileMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface PostMapper {
    PostDto toDto(Post post);

    default PostDto toDetailedDto(PostDetailed postDetailed){
        var dto = toDto(postDetailed.getPost());
        dto.setLiked(postDetailed.isLiked());
        return dto;
    }
}
