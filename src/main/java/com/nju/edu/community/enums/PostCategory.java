package com.nju.edu.community.enums;

public enum PostCategory {
    LegalAdvice("法律咨询"),
    FinancialProblem("金融问题"),
    Patent("专利方面"),
    Other("其他问题");

    private String content;

    private PostCategory(String content){
        this.content=content;
    }

    public String getChinese(){
        return this.content;
    }
}
