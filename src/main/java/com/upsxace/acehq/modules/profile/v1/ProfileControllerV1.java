package com.upsxace.acehq.modules.profile.v1;

import com.upsxace.acehq.config.clerk.UserContext;
import com.upsxace.acehq.modules.profile.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
public class ProfileControllerV1 {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserContext> me(){
        return ResponseEntity.ok(userService.getUserContext());
    }
}
