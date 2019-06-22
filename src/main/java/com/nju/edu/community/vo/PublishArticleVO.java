package com.nju.edu.community.vo;

import com.nju.edu.community.enums.PostTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PublishArticleVO {
    private String post_id;
    private String author;
    private String post_name;
    private PostTag post_tag;
    private String brief_intro;
    private String content;

}
