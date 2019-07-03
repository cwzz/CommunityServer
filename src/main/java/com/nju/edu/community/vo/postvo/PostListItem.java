package com.nju.edu.community.vo.postvo;

import com.nju.edu.community.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostListItem {

    private String postId;
    private boolean set_top;
    private String tag;
    private String title;
    private String author;
    private int reply;
    private int view;

    private String nickname;

    public PostListItem(Post post){
        this.postId=post.getPid();
        this.set_top=post.isSetTop();
        this.tag=post.getPostTag();
        this.title=post.getTitle();
        this.author=post.getAuthor();
        this.reply=post.getRemarkNum();
        this.view=post.getVisits();
    }

    public PostListItem(Post post, String nickname){
        this.postId=post.getPid();
        this.set_top=post.isSetTop();
        this.tag=post.getPostTag();
        this.title=post.getTitle();
        this.author=post.getAuthor();
        this.reply=post.getRemarkNum();
        this.view=post.getVisits();
        this.nickname=nickname;
    }
}
