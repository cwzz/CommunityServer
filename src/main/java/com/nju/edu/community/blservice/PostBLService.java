package com.nju.edu.community.blservice;

import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.postvo.PostVO;
import com.nju.edu.community.vo.RecordVO;
import com.nju.edu.community.vo.postvo.PostListItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;


public interface PostBLService {
    ArrayList<PostListItem> getArticleList(String category, String tag);
    ArrayList<PostListItem> getMyRelease(String email);
    ArrayList<PostListItem> getMyCollectPost(ArrayList<String> postIds);

    String createID(String author);
    ResultMessage publishArticle(String postID, String author, String postTitle, String category,String postTag, String briefIntro, String content) throws IOException;
    ResultMessage edit(String post_id, String post_name, String post_tag, String content_url);
    ResultMessage deleteArticle(String post_id);
    ResultMessage remark(String post_id, String reviewer, String remark_content);
    PostVO readArticle(String post_id) throws IOException;
    ArrayList<RecordVO> recommend(String author);
    String downLoadFromUrl(String urlStr) throws IOException;


    ResultMessage addInterestNum(String post_id);
    ResultMessage minusInterestNum(String post_id);

    String uploadFile(String post_id, MultipartFile file);
}
