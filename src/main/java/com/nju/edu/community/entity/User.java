package com.nju.edu.community.entity;

import com.nju.edu.community.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String uid;//用户的ID，为注册邮箱

    private String password;//用户

    private String code;//邮箱验证码

    private boolean active;//用户身份已激活

    private String nickname;//用户的昵称

    private String image;//用户头像url

    @Enumerated(EnumType.STRING)
    private Sex sex;//性别

    private String introduce;//个人简介

    private String birthday;//生日

    private String tags;//用户的标签，感兴趣的方面？用逗号分隔

    private int releasedNum;//用户自己发布的帖子数

    private int interestNum;//用户关注的用户数

    private int collectNum;//用户收藏的帖子数

    private int fansNum;//用户的粉丝数

    //用户收藏的帖子
    @ElementCollection(targetClass = String.class)
    private List<String> collectPost;//收藏的帖子

    //用户的粉丝
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NameAndImage> fans;

    //用户关注的人
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NameAndImage> interestUser;

}
