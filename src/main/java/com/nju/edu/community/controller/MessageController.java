package com.nju.edu.community.controller;

import com.nju.edu.community.blservice.MessageBLService;
import com.nju.edu.community.entity.Message;
import com.nju.edu.community.vo.uservo.StringVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/Message")
public class MessageController {

    @Autowired
    private MessageBLService messageBLService;


    @RequestMapping(value = "/showMessage",method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<Message> showMessage(@RequestBody StringVO stringVO){
        return messageBLService.getAllMessageList(stringVO.getEmail());
    }

}
