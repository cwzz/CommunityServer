package com.nju.edu.community.controller;

import com.nju.edu.community.blservice.UserBLService;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.uservo.*;
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

    /**
     * 获取注册验证码
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage sendCode(@RequestBody StringVO email){
        return userBLService.getCode(email.getEmail());
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/registerBack" , method = RequestMethod.POST)
    public @ResponseBody ResultMessage register(@RequestBody RegisterReq registerReq){
        System.out.println(registerReq.toString());
        return userBLService.register(registerReq.getEmail(), registerReq.getPassword(), registerReq.getCode());
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/loginBack", method = RequestMethod.POST)
    public @ResponseBody ResultMessage login(@RequestBody LoginReq req){
        return userBLService.login(req.getEmail(), req.getPassword());
    }

    /**
     * 忘记密码
     */
    @RequestMapping(value = "/forgetPass", method = RequestMethod.POST)
    public @ResponseBody ResultMessage forgetPass(@RequestBody StringVO email){
        return userBLService.forgetPass(email.getEmail());
    }

    /**
     * 获得用户头像
     */
    @RequestMapping(value = "/getImageUrl", method = RequestMethod.POST)
    public @ResponseBody String resetPass(@RequestBody StringVO email){
        return userBLService.getImageUrl(email.getEmail());
    }

    /**
     * 获得个人中心的信息
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public @ResponseBody
    UserInfoVO getUserInfo(@RequestBody StringVO email){
        return userBLService.getUserInfo(email.getEmail());
    }

    /**
     * 修改个人中心信息
     */
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage modifyUserInfo(@RequestBody UserInfoVO userInfoVO){
        return userBLService.modifyUserInfo(userInfoVO);
    }

    /**
     * 修改用户头像
     */
    @RequestMapping(value = "/modifyImage", method = RequestMethod.POST)
    public @ResponseBody
    String modifyImage(UploadImageVO imageVO){
        return userBLService.modifyImage(imageVO.getEmail(), imageVO.getFile());
    }
}
