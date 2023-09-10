package com.example.model.ResponceDTO;

import lombok.*;

import java.util.List;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasketResponseDTO {
    private UUID basketId;
    private UUID userId;
    private List<BasketProductDTO> products;
    private  double totalPrice;
}
