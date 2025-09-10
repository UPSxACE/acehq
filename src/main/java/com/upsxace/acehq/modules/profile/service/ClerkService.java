package com.upsxace.acehq.modules.profile.service;

import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.models.components.EmailAddress;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.operations.UpdateUserMetadataRequestBody;
import com.clerk.backend_api.models.operations.UpdateUserRequestBody;
import com.upsxace.acehq.modules.profile.dto.PublicMetadataDto;
import com.upsxace.acehq.modules.profile.dto.ClerkUserDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClerkService {
    @Value("${clerk.secret}")
    private String clerkSecret;
    private Clerk sdk;

    @PostConstruct
    private void init() {
        this.sdk = Clerk.builder()
                .bearerAuth(clerkSecret)
                .build();
    }

    private String getEmail(User user) {
        var primaryEmailId = user.primaryEmailAddressId();
        if (primaryEmailId.isEmpty()) return null;

        var emailObj = user
                .emailAddresses()
                .stream()
                .filter(e -> e.id().equals(primaryEmailId))
                .findFirst();

        return emailObj.map(EmailAddress::emailAddress).orElse(null);
    }

    public Optional<ClerkUserDto> getUser(String id) {
        try {
            var response = sdk.users().get().userId(id).call().user();
            return response.map(user -> new ClerkUserDto(
                    user.username().orElse(null),
                    getEmail(user)
            ));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void updateUser(String id, ClerkUserDto user) {
        try {
            sdk.users().update().userId(id)
                    .requestBody(UpdateUserRequestBody
                            .builder()
                            .username(user.getUsername())
                            .build()
                    )
                    .call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void updateMetadata(String id, PublicMetadataDto metadata) {
        try {
            sdk.users().updateMetadata().userId(id)
                    .requestBody(
                            UpdateUserMetadataRequestBody
                                    .builder()
                                    .publicMetadata(metadata.toMap())
                                    .build()
                    )
                    .call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
