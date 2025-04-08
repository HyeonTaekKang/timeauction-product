package com.timeAuction.timeProduct.controller.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        return "메인페이지입니다";
   }
}
