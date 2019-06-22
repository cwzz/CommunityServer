package com.nju.edu.community.blservice;

import com.nju.edu.community.enums.PostTag;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.BriefPost;
import com.nju.edu.community.vo.PostVO;
import com.nju.edu.community.vo.RecordVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;


public interface PostBLService {
    String createID(String author);
    ResultMessage publishArticle(String post_id, String author, String post_name, PostTag post_tag, String brief_intro, String content) throws IOException;
    ResultMessage edit(String post_id, String post_name, PostTag post_tag, String content_url);
    ResultMessage deleteArticle(String post_id);
    ResultMessage remark(String post_id, String reviewer, String remark_content);
    PostVO readArticle(String post_id, String reader) throws IOException;
    ArrayList<BriefPost> readArticleList(String author);
    ArrayList<BriefPost> getAllArticleList();
    ArrayList<RecordVO> recommend(String author);
    ArrayList<BriefPost> searchArticle(String keywords);
    String downLoadFromUrl(String urlStr) throws IOException;


    ResultMessage addInterestNum(String post_id);
    ResultMessage minusInterestNum(String post_id);
    BriefPost getBriefPostByID(String post_id);


//    void saveAsFile(String content);
    String uploadFile(String post_id, MultipartFile file);
}