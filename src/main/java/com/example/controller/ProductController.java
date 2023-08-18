package com.example.controller;

import com.example.model.ProductCreateDTO;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public String create
            (
                    @ModelAttribute ProductCreateDTO dto,
                    Model model
            ) {
        productService.create(dto);
        model.addAttribute("products",productService.getAll(dto.getSellerId()));
        return "seller/products";
    }
}
