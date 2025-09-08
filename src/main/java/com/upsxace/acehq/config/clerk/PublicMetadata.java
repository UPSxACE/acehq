package com.upsxace.acehq.config.clerk;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PublicMetadata {
    private final String id;
    private final String authorities;
}
