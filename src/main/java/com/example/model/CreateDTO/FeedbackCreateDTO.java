package com.example.model.CreateDTO;

import lombok.*;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackCreateDTO {
    private UUID productId;
    private UUID userId;
    private int rate;
    private String text;
}
