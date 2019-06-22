package com.nju.edu.community.vo.postvo;

import com.nju.edu.community.enums.PostCategory;
import com.nju.edu.community.enums.PostTag;
import lombok.Data;

@Data
public class SearchReq {

    private PostCategory category;

    private PostTag label;
}
