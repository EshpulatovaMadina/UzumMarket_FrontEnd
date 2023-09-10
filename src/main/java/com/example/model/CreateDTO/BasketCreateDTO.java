package com.example.model.CreateDTO;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasketCreateDTO {
   private UUID userId;
    private UUID productId;
    private  int count;
}
