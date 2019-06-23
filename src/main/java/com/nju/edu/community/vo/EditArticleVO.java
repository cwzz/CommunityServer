package com.nju.edu.community.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditArticleVO {
    private String post_id;
    private String post_name;
    private String post_tag;
    private String content;
}
