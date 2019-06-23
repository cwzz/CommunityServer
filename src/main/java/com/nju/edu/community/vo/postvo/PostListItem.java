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

    public PostListItem(Post post, boolean set_top){
        this.postId=post.getPid();
        this.set_top=set_top;
        this.tag=post.getPostTag();
        this.title=post.getTitle();
        this.author=post.getAuthor();
        this.reply=post.getRemarkNum();
        this.view=post.getVisits();
    }
}