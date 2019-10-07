package com.example.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.proj.User;
import com.example.service.UserService;


@Controller
public class UserController
{
    
    private Logger logger =Logger.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    
    @RequestMapping("/getUserInfo/{name}")
    @ResponseBody
    public ModelAndView getUserInfo(@PathVariable String name) {
        User user = userService.findByName(name);
        if(user!=null){
            System.out.println("user.getName():"+user.getName());
            logger.info("user.getPwd():"+user.getPwd());
        }
        return new ModelAndView("login");
    }
    
    @RequestMapping("/getUserInfo1")
    @ResponseBody
    public ModelAndView getUserInfo1(@RequestParam(value="name") String name, Model model) {
        User user = userService.findByName(name);
        model.addAttribute("name", user);
        if(user==null){
            return new ModelAndView("error");
        }
        System.out.println("user.getName():"+user.getName());
        logger.info("user.getPwd():"+user.getPwd());
        return new ModelAndView("login");
    }
    
}
