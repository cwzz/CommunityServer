package com.nju.edu.community.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPicVO {

    private String base64;
    private String filename;
    private String projectID;

}
