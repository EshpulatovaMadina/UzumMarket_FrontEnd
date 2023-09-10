package com.example.model.ResponceDTO;

import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BasketProductDTO {
    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private Double price;
    private Integer count;
}
