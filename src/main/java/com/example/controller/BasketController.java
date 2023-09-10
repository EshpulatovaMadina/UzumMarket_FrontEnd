package com.example.controller;

import com.example.model.CreateDTO.BasketCreateDTO;
import com.example.model.CreateDTO.ProductCreateDTO;
import com.example.model.ResponceDTO.BasketResponseDTO;
import com.example.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping("/create")
    public String create(
            @ModelAttribute BasketCreateDTO dto,
            Model model) {
        String s = basketService.create(dto);
        model.addAttribute("baskets", basketService.basketsOfUser(dto.getUserId()));
        if (Objects.equals(s, "saved")) {
            return "/user/basket";
        } else {

            return "/user/basket";
        }
    }

    @GetMapping("/basketsOfUser")
    public String basketsOfUser(
            @RequestParam UUID userId,
            Model model){
        BasketResponseDTO basketResponseDTOS = basketService.basketsOfUser(userId);
        model.addAttribute("basket", basketResponseDTOS);
        return "/user/basket";
    }

    @PutMapping("/updateCount{productId}")
    public String updateCount(@PathVariable UUID productId, @RequestParam UUID basketId,int count,  Model model){
        BasketResponseDTO basketResponseDTO = basketService.updateCount(productId, basketId, count);
        model.addAttribute("basket", basketResponseDTO);
        return "/user/basket";
    }



}
