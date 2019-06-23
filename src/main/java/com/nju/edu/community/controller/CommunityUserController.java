package com.nju.edu.community.controller;

import com.nju.edu.community.blservice.CommunityUserBLService;
import com.nju.edu.community.entity.Mine;
import com.nju.edu.community.vo.BriefPost;
import com.nju.edu.community.vo.BriefUser;
import com.nju.edu.community.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/C_User")
public class CommunityUserController {

    @Autowired
    private CommunityUserBLService blService;

    @RequestMapping("/search")
    public @ResponseBody
    List<BriefUser> searchByKeyword(String keyword){
        return blService.searchByKeyword(keyword);
    }

    @RequestMapping("/interestpost")
    public @ResponseBody
    void attentionPost(String userID,String postID){
        blService.interestPost(userID,postID);
    }

    @RequestMapping("/interestuser")
    public @ResponseBody
    void attentionUser(String starID,String fanID){
        blService.interestUser(starID,fanID);
    }

    @RequestMapping("/uninterestpost")
    public @ResponseBody
    void unInterestPost(String userID,String postID){
        blService.cancelInterest(userID,postID);
    }

    @RequestMapping("/uninterestuser")
    public @ResponseBody
    void unInterestUser(String starID,String fanID){
        blService.cancelInterestUser(starID,fanID);
    }

    @RequestMapping("/getRelease")
    public @ResponseBody
    List<BriefPost> getRelease(String userID){
        return blService.getReleased(userID);
    }

    @RequestMapping("/getInterestedPost")
    public @ResponseBody
    List<BriefPost> getInterestedPost(String userID){
        return blService.getInterestedpost(userID);
    }

    @RequestMapping("/getCollect")
    public @ResponseBody
    List<Mine> getCollect(String userID){
        return blService.getCollected(userID);
    }

    @RequestMapping("/getInterestedUser")
    public @ResponseBody
    List<BriefUser> getInterestedUser(String userID){
        return blService.getInterestedUser(userID);
    }

    @RequestMapping("/getHistory")
    public @ResponseBody
    List<RecordVO> getHistory(String userID){
        return blService.getHistory(userID);
    }

}
