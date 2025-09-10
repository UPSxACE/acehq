package com.upsxace.acehq.modules.profile.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SafeURLValidator implements ConstraintValidator<SafeURL, String> {
    @Value("${app.frontend-host")
    private String frontendHost;

    @Value("${app.backend-host")
    private String backendHost;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null) return true;

        String escapedFrontendHost = Pattern.quote(frontendHost);
        String escapedBackendHost = Pattern.quote(backendHost);
        String REGEX = "https?://(" + escapedFrontendHost + "|" + escapedBackendHost + ")/.*";
        return s.matches(REGEX);
    }
}
