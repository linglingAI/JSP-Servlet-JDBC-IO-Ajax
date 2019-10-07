package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class PageController
{
    @RequestMapping("/login") 
    public ModelAndView index() {
        return new ModelAndView("login");
    }
    
    @RequestMapping("/login1") 
    public String index1() {
        return "pages/login";
    }
    
   
}