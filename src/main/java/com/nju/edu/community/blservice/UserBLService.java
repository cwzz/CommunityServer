package com.nju.edu.community.blservice;

import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.uservo.UserInfoVO;


public interface UserBLService {

    ResultMessage getCode(String email);

    //用户邮箱注册
    ResultMessage register(String email,String password,String code);

    //登录
    ResultMessage login(String email, String password);

    //忘记密码
    ResultMessage forgetPass(String email);

    //获取用户的头像的url
    String getImageUrl(String username);

    //获得用户的个人中心基本信息
    UserInfoVO getUserInfo(String userID);

    //修改用户个人中心信息
    ResultMessage modifyUserInfo(UserInfoVO userInfoVO);

    //修改用户头像


}
