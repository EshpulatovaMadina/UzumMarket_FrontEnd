package com.example.model.ResponceDTO;

import lombok.*;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackResponseDTO {
    private UUID productId;
    private String userName;
    private int rate; /// yulduzchalari
    private String text;
}
