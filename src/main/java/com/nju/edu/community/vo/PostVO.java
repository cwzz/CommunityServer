package com.nju.edu.community.vo;

import com.nju.edu.community.entity.Post;
import com.nju.edu.community.entity.Remark;
import com.nju.edu.community.enums.PostTag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class PostVO {
    private String post_id;
    private String author;
    private String post_name;
    private PostTag post_tag;
    private String content;
    private long publish_time;
    private long visits;
    private long remark_num;
    private long interest_num;//关注数
    private ArrayList<Remark> remark_content;

    public PostVO(){}

    public PostVO(Post post){
        this.post_id=post.getPid();
        this.author=post.getAuthor();
        this.post_name=post.getTitle();
        this.post_tag=post.getPostTag();
        this.content=post.getContentUrl();
        this.publish_time=post.getPublishTime();
        this.visits=post.getVisits();
        this.remark_num=post.getRemarkNum();
        this.interest_num=post.getInterestNum();
        this.remark_content=new ArrayList<>(post.getRemarkList());
    }



}
