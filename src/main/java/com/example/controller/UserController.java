package com.example.controller;

import com.example.model.SignInDTO;
import com.example.model.UserCreateDTO;
import com.example.model.UserResponseDTO;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/sign-up")
    public String getCreate(){
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String createUser
            (
                    @ModelAttribute UserCreateDTO dto
            )
    {
        System.out.println("dto = " + dto);
        userService.createUser(dto);
        return "index";
    }

    @GetMapping("/sign-in")
    public String signIn
            (
                    @ModelAttribute SignInDTO dto,
                    Model model
            )

    {
        UserResponseDTO userResponseDTO = userService.signIn(dto);
        if(userResponseDTO == null){
            return "index";
        }else {
            model.addAttribute("user",userResponseDTO);

            switch (userResponseDTO.getUserRole()) {
                case "USER"-> {
                    return "user/menu";
                }
                case "ADMIN" -> {
                    return "admin/menu";
                }
                case "SELLER" -> {
                    return "seller/menu";
                }
                default -> {
                    return "index";
                }
            }
        }
    }

    @GetMapping("/sign-in-get")
    public String signIN() {
        return "auth/sign-in";
    }
}
