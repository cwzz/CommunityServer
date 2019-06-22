package com.nju.edu.community.vo.postvo;

import com.nju.edu.community.enums.PostTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostListItem {

    private String postId;
    private boolean set_top;
    private PostTag tag;
    private String title;
    private String author;
    private int reply;
    private int view;
}
