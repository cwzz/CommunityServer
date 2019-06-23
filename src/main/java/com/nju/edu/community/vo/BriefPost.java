package com.nju.edu.community.vo;


import com.nju.edu.community.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BriefPost {
    private String post_id;
    private String author;
    private String post_name;
    private String post_tag;
    private String brief_intro;
    private long publish_time;
    private long visits;
    private long remark_num;

    public BriefPost(){}

    public BriefPost(Post post){
        this.post_id=post.getPid();
        this.author=post.getAuthor();
        this.post_name=post.getTitle();
        this.post_tag=post.getPostTag();
        this.brief_intro=post.getBriefIntro();
        this.publish_time=post.getPublishTime();
        this.visits=post.getVisits();
        this.remark_num=post.getRemarkNum();
    }
}
