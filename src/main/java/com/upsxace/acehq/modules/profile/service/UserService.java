package com.upsxace.acehq.modules.profile.service;

import com.upsxace.acehq.config.error.BadRequestException;
import com.upsxace.acehq.modules.profile.dto.PublicMetadataDto;
import com.upsxace.acehq.config.clerk.UserContext;
import com.upsxace.acehq.config.error.ForbiddenException;
import com.upsxace.acehq.modules.profile.dto.ClerkUserDto;
import com.upsxace.acehq.modules.profile.dto.CompleteProfileRequest;
import com.upsxace.acehq.modules.profile.dto.MeDto;
import com.upsxace.acehq.modules.profile.dto.ProfileDto;
import com.upsxace.acehq.modules.profile.entity.Profile;
import com.upsxace.acehq.modules.profile.entity.ProfileType;
import com.upsxace.acehq.modules.profile.entity.UserRole;
import com.upsxace.acehq.modules.profile.mapper.ProfileMapper;
import com.upsxace.acehq.modules.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ProfileRepository profileRepository;
    private final ClerkService clerkService;
    private final ProfileMapper profileMapper;

    public Optional<UserContext> getUserContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        return Optional.of((UserContext) authentication.getPrincipal());
    }

    public Optional<MeDto> getMe() {
        var userCtx = getUserContext().orElse(null);
        if (userCtx == null) return Optional.empty();
        if (userCtx.getId() == null) return Optional.of(new MeDto(null, userCtx.getAuthoritiesString(), null));

        var profile = profileRepository.findById(userCtx.getId()).orElseThrow(IllegalStateException::new);
        return Optional.of(new MeDto(userCtx.getId(), userCtx.getAuthoritiesString(), profileMapper.toDto(profile)));
    }

    @Transactional
    public ProfileDto completeUserProfile(CompleteProfileRequest request) {
        var userCtx = getUserContext().orElse(null);
        if (userCtx == null)
            throw new IllegalStateException();
        if (userCtx.getId() != null && userCtx.getAuthorities() != null) // TODO: instead of checking like this, check if all profile mandatory fields are filled, or just if profile exists
            throw new ForbiddenException();

        var profile = userCtx.getId() == null
                ? new Profile()
                : profileRepository.findById(userCtx.getId()).orElseThrow(IllegalStateException::new);

        // Check clerkId conflicts
        if (
            // check if the clerkId of the profile found doesn't match the current user clerkId
                (profile.getClerkId() != null && !userCtx.getClerkId().equals(profile.getClerkId()))
                        // check if, since this profile is new(has no clerkId), there is any account with this clerkId already
                        | (profile.getClerkId() == null && profileRepository.exists(Example.of(Profile.builder().clerkId(userCtx.getClerkId()).build())))
        )
            throw new IllegalStateException();

        // automatic fields
        profile.setClerkId(userCtx.getClerkId());
        if (profile.getType() == null) profile.setType(ProfileType.USER);
        if (profile.getRole() == null) profile.setRole(UserRole.USER);

        // request fields
        profile.setUsername(request.getUsername());
        profile.setName(Optional.ofNullable(request.getName()).orElse(""));
        profile.setAvatar(request.getAvatar());

        // clerk fields that are not request fields
        var clerkUser = clerkService.getUser(userCtx.getClerkId()).orElseThrow(IllegalStateException::new);
        profile.setEmail(clerkUser.getEmail());

        // check conflicts
        var existingUsername = profileRepository.findByUsername(profile.getUsername());
        if(existingUsername.isPresent() && existingUsername.get().getId() != profile.getId())
            throw new BadRequestException("Username is already taken.");
        var existingEmail = profileRepository.findByEmail(profile.getEmail());
        if(existingEmail.isPresent() && existingEmail.get().getId() != profile.getId())
            throw new BadRequestException("Email is already taken.");

        // update user data
        profileRepository.saveAndFlush(profile);
        // update clerk data and metadata
        clerkService.updateUser(userCtx.getClerkId(), ClerkUserDto.fromProfile(profile));
        clerkService.updateMetadata(userCtx.getClerkId(), new PublicMetadataDto(profile.getId(), "ROLE_" + profile.getRole()));

        return profileMapper.toDto(profile);
    }
}
