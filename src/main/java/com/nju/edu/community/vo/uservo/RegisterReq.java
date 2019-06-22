package com.nju.edu.community.vo.uservo;

import lombok.Data;

@Data
public class RegisterReq {


    //注册邮箱
    private String email;

    //注册密码
    private String password;

    //注册验证码
    private String code;

}
