package com.nju.edu.community.blservice;

import com.nju.edu.community.enums.ResultMessage;


public interface UserBLService {

    ResultMessage getCode(String email);

    //用户邮箱注册
    ResultMessage register(String email,String password,String code);

    //登录
    ResultMessage login(String email, String password);

    //忘记密码
    ResultMessage forgetPass(String email);

    ResultMessage resetPassWhenForget(String email, String pass, String code);

    //获取用户的头像的url
    String getImageUrl(String username);


}
