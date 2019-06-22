package com.nju.edu.community.entity;

import com.nju.edu.community.enums.PostCategory;
import com.nju.edu.community.enums.PostState;
import com.nju.edu.community.enums.PostTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String pid;//文章ID
    private String author;//文章作者，邮箱号
    private String title;//文章标题
    private String brief_intro;//文章简介

    @Enumerated(EnumType.STRING)
    private PostCategory category;//文章分类

    @Enumerated(EnumType.STRING)
    private PostTag postTag;//文章标签

    private PostState state;//文章的状态
    private boolean setTop;//文章是否置顶
    private String contentUrl;//文章内容URL,存储在OSS上

    private long publishTime;//发布时间
    private int visits;//浏览数
    private int remarkNum;//评论数
    private int interestNum;//被收藏的次数

    @OneToMany(cascade = CascadeType.ALL)
    private List<Remark> remarkList;//文章下的评论列表

    public Post(String author){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String currentTime=df.format(new Date());// new Date()为获取当前系统时间
        this.pid =currentTime+author.substring(0,author.indexOf('@'));
        this.author=author;
        this.state=PostState.Draft;
    }
}
