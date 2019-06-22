package com.nju.edu.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/test")
    public String test(){
        return "Test";
    }

    @RequestMapping("/detail")
    public String getDetail(){
        return "community/community_detail";
    }

    @RequestMapping("/home")
    public String getHome(){
        return "community/community_home";
    }

    @RequestMapping("/interest")
    public String getInterest(){
        return "community/community_interest";
    }

    @RequestMapping("/person")
    public String getPerson(){
        return "community/community_person";
    }

    @RequestMapping("/publish")
    public String getPublish(){
        return "community/community_publish";
    }

    @RequestMapping("/search")
    public String getSearch(){
        return "community/community_search";
    }
}
