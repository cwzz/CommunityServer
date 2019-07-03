package com.nju.edu.community.controller;

import com.nju.edu.community.blservice.CommunityUserBLService;
import com.nju.edu.community.entity.NameAndImage;
import com.nju.edu.community.vo.StarVO;
import com.nju.edu.community.vo.postvo.PostListItem;
import com.nju.edu.community.vo.uservo.StringVO;
import com.nju.edu.community.vo.uservo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/C_User")
public class CommunityUserController {

    @Autowired
    private CommunityUserBLService blService;

    @RequestMapping("/search")
    public @ResponseBody
    List<UserInfoVO> searchByKeyword(@RequestBody StringVO stringVO){
        return blService.searchByKeyword(stringVO.getEmail());
    }

    /**
     * 得到当前用户是否收藏了这个帖子
     */
    @RequestMapping(value = "/judgeCollect", method = RequestMethod.POST)
    public @ResponseBody
    boolean judgeCollect(@RequestBody StarVO starVO){
        return blService.judgeCollect(starVO.getCurrentUser(), starVO.getParam());
    }

    /**
     * 得到当前用户是否关注了这个用户
     */
    @RequestMapping(value = "/judgeStar", method = RequestMethod.POST)
    public @ResponseBody
    boolean judgeStar(@RequestBody StarVO starVO){
        return blService.judgeStar(starVO.getCurrentUser(), starVO.getParam());
    }

    /**
     * 收藏帖子
     */
    @RequestMapping(value = "/collectPost", method = RequestMethod.POST)
    public @ResponseBody
    void attentionPost(@RequestBody StarVO starVO){
        blService.interestPost(starVO.getCurrentUser(),starVO.getParam());
    }

    /**
     * 取消收藏帖子
     */
    @RequestMapping(value = "/cancelCollectPost", method = RequestMethod.POST)
    public @ResponseBody
    void cancelAttentionPost(@RequestBody StarVO starVO){
        blService.cancelInterestPost(starVO.getCurrentUser(), starVO.getParam());
    }

    /**
     * 关注用户
     */
    @RequestMapping(value = "/starUser",method = RequestMethod.POST)
    public @ResponseBody
    void starUser(@RequestBody StarVO starVO){
        blService.starUser(starVO.getCurrentUser(),starVO.getParam());
    }

    /**
     * 取消关注
     */
    @RequestMapping(value = "/cancelStarUser",method = RequestMethod.POST)
    public @ResponseBody
    void cancelStarUser(@RequestBody StarVO starVO){
        blService.cancelStarUser(starVO.getCurrentUser(),starVO.getParam());
    }

    /**
     * 获得我发布的帖子
     */
    @RequestMapping(value = "/getMyRelease",method = RequestMethod.POST)
    public @ResponseBody
    List<PostListItem> getMyReleasePost(@RequestBody StringVO stringVO){
        return blService.getReleased(stringVO.getEmail());
    }

    /**
     * 获得我收藏的帖子
     */
    @RequestMapping(value = "/getMyCollect", method = RequestMethod.POST)
    public @ResponseBody
    List<PostListItem> getMyCollectPost(@RequestBody StringVO stringVO){
        return blService.getCollected(stringVO.getEmail());
    }

    /**
     * 获得我关注的人
     */
    @RequestMapping(value = "/getMyStarUser", method = RequestMethod.POST)
    public @ResponseBody
    List<NameAndImage> getInterestedUser(@RequestBody StringVO stringVO){
        return blService.getInterestedUser(stringVO.getEmail());
    }

    /**
     * 获得我的粉丝
     */
    @RequestMapping(value = "/getMyFans", method = RequestMethod.POST)
    public @ResponseBody
    List<NameAndImage> getMyFans(@RequestBody StringVO stringVO){
        return blService.getFans(stringVO.getEmail());
    }

}
