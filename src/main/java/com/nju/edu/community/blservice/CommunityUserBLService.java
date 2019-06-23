package com.nju.edu.community.blservice;

import com.nju.edu.community.entity.NameAndImage;
import com.nju.edu.community.vo.postvo.PostListItem;
import com.nju.edu.community.vo.uservo.UserInfoVO;

import java.util.List;

public interface  CommunityUserBLService {

    //模糊查找，根据关键词搜素用户
    List<UserInfoVO> searchByKeyword(String keyword);

    //判断用户是否收藏了帖子
    boolean judgeCollect(String user, String postID);

    boolean judgeStar(String currentUser, String user);

    //用户收藏帖子
    void interestPost(String username, String postID);
    //用户取消收藏帖子
    void cancelInterestPost(String username, String postID);
    //用户关注其他用户
    void starUser(String fansID, String beStarID);
    //用户取消关注某用户
    void cancelStarUser(String starID, String fanID);

    //获取用户发布的帖子列表
    List<PostListItem> getReleased(String username);

    //获取用户收藏的帖子列表
    List<PostListItem> getCollected(String username);

    //获取用户关注的用户列表
    List<NameAndImage> getInterestedUser(String username);

    //获取用户的粉丝
    List<NameAndImage> getFans(String userID);

    //用户发布文章
    void releasePost(String useID);

}
