package com.upsxace.acehq.modules.profile.controller.v1;

import com.upsxace.acehq.modules.profile.dto.MeDto;
import com.upsxace.acehq.modules.profile.dto.ProfileDto;
import com.upsxace.acehq.modules.profile.service.UserService;
import com.upsxace.acehq.modules.profile.dto.CompleteProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
public class ProfileControllerV1 {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<MeDto> me(){
        return ResponseEntity.ok(userService.getMe().orElse(null));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<ProfileDto> completeProfile(
            @RequestBody @Valid CompleteProfileRequest request
    ){
        return ResponseEntity.ok(userService.completeUserProfile(request));
    }
}
