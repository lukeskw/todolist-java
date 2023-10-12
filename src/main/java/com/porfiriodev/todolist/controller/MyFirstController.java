package com.porfiriodev.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-route")
public class MyFirstController {
    @GetMapping("/")
    public String message(){
        return "ok";
    }
}
