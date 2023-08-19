package com.example.controller;

import com.example.model.ProductCreateDTO;
import com.example.model.ProductResponseDTO;
import com.example.service.ProductService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/create")
    public String create
            (
                    @ModelAttribute ProductCreateDTO dto,
                    Model model
            ) {
        productService.create(dto);
        Page<ProductResponseDTO> products = productService.getAll(dto.getSellerId());
        model.addAttribute("products",products);
        return "seller/products";
    }
}
