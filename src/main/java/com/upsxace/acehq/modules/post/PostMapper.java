package com.upsxace.acehq.modules.post;

import com.upsxace.acehq.modules.profile.mapper.ProfileMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface PostMapper {
    PostDto toDto(Post post);
}
