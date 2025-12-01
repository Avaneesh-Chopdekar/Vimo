package com.avaneesh.vimo_backend.common.payloads;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomMessage<T> {
    private String message;
    @Builder.Default
    private boolean success = false;
    private T data;
}