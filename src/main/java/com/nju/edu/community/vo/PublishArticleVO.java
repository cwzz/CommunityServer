package com.nju.edu.community.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishArticleVO {
    private String postId;
    private String author;
    private String postTitle;
    private String category;
    private String postTag;
    private String briefIntro;
    private String content;

}
