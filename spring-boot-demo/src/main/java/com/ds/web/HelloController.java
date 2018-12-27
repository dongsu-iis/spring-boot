package com.ds.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    // @RequestMapping(value = "/say",method = RequestMethod.GET)
    @PostMapping("/say") // 略
    public String hello(){
        return "Hello Spring Boot";
    }
}
