package com.nju.edu.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NameAndImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nid;

    private String email;

    private String name;

    private String img;

    private boolean isFollow;//我的粉丝是否互关

}
