package com.nju.edu.community.vo;

import com.nju.edu.community.enums.PostCategory;
import com.nju.edu.community.enums.PostTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishArticleVO {
    private String postId;
    private String author;
    private String postTitle;
    private PostCategory category;
    private PostTag postTag;
    private String briefIntro;
    private String content;

}
