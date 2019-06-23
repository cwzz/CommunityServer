package com.nju.edu.community.vo.uservo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageVO {

    private String email;

    private MultipartFile file;
}
