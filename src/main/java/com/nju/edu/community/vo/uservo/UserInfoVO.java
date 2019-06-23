package com.nju.edu.community.vo.uservo;

import com.nju.edu.community.entity.User;
import com.nju.edu.community.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserInfoVO {

    private String email;//用户的ID，与非社区部分的ID保持一直，默认为注册时的手机号码或者邮箱

    private String imageUrl;//用户头像的url

    private String nickname;//用户的昵称

    private Sex gender;//性别

    private String desc;//简介

    private String birth;//生日

    private String[] interest;//用户感兴趣的标签

    private int releasedNum;//用户发帖数

    private int interestUserNums;//用户的关注数

    private int collectPostNums;//用户收藏的帖子数

    private int fansNums;//用户的粉丝数


    public UserInfoVO(User user){
        this.email=user.getUid();
        this.imageUrl=user.getImage();
        this.nickname=user.getNickname();
        this.gender=user.getSex();
        this.desc=user.getIntroduce();
        this.birth=user.getBirthday();
        this.interest=user.getTags().split(",");

        this.releasedNum=user.getReleasedNum();
        this.interestUserNums=user.getInterestNum();
        this.collectPostNums=user.getCollectNum();
        this.fansNums=user.getFansNum();
    }

}
