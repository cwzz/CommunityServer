package com.nju.edu.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "remark")
@AllArgsConstructor
public class Remark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rid;
    private String post_id;
    private String remark_content;
    private String reviewer;
    private String review_img;
    private String nickname;
    private long remark_time;

    public Remark(){}

    public Remark(String post_id,String remark_content,String reviewer,String review_img, String nickname){
        this.post_id=post_id;
        this.remark_content=remark_content;
        this.reviewer=reviewer;
        this.remark_time=new Date().getTime();
        this.review_img=review_img;
        this.nickname=nickname;
    }

}
