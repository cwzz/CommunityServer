package com.nju.edu.community.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemarkVO {

    private String post_id;
    private String reviewer;
    private String remark_content;

}
