package com.nju.edu.community.controller;

import com.nju.edu.community.blservice.UserBLService;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.uservo.EmailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserBLService userBLService;

    /*
    获得验证码
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage sendCode(@RequestBody EmailVO email){
        return userBLService.getCode(email.getEmail());
    }

    /**
     * sign up by email
     * @param email 用户邮箱
     * @param pass 密码
     * @param code 验证码
     * @return 注册结果
     */
    @RequestMapping(value = "/registerBack" , method = RequestMethod.POST)
    public @ResponseBody ResultMessage register(String email, String pass, String code){
        return userBLService.register(email, pass, code);
    }

    @RequestMapping(value = "/loginBack", method = RequestMethod.POST)
    public @ResponseBody ResultMessage login(String email, String pass){
        return userBLService.login(email,pass);
    }

    @RequestMapping(value = "/forgetPass")
    public @ResponseBody ResultMessage forgetPass(String email){
        return userBLService.forgetPass(email);
    }

    @RequestMapping(value = "/resetPass")
    public @ResponseBody ResultMessage resetPass(String email, String newPass, String code){
        return userBLService.resetPassWhenForget(email, newPass, code);
    }





}
