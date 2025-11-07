package com.finance.common.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private Meta meta;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String message;

        @Builder.Default
        private OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
