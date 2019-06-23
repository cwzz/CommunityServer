package com.nju.edu.community.vo.postvo;

import com.nju.edu.community.entity.Post;
import com.nju.edu.community.entity.Remark;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class PostVO {
    private String postId;//文章ID
    private String author;//文章作者
    private String postTitle;//文章标题
    private String briefIntro;//文章简介
    private String postCategory;//文章分类
    private String postTag;//文章标签
    private String content;//文章内容
    private long publish_time;//发布时间
    private int visits;//浏览数
    private int remarkNum;//评论数
    private long interestNum;//被收藏的次数
    private ArrayList<Remark> remark_content;

    public PostVO(){}

    public PostVO(Post post, String content, ArrayList<Remark> remarks){
        this.postId =post.getPid();
        this.author=post.getAuthor();
        this.postTitle =post.getTitle();
        this.briefIntro=post.getBriefIntro();
        this.postCategory=post.getCategory();
        this.postTag =post.getPostTag();
        this.content=content;
        this.publish_time=post.getPublishTime();
        this.visits=post.getVisits();
        this.remarkNum =post.getRemarkNum();
        this.interestNum =post.getInterestNum();
        this.remark_content=remarks;
    }



}
