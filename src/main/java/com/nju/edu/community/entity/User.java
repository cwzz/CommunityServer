package com.nju.edu.community.entity;

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

    private String tags;//用户的标签，感兴趣的方面？用逗号分隔

    private int releasedNum;//用户自己发布的帖子数

    private int interestNum;//用户关注的用户数

    private int collectNum;//用户收藏的帖子数

    private int fansNum;//用户的粉丝数

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Mine> mines;//与用户相关的帖子或用户的列表

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Record> history;//用户的浏览记录

}