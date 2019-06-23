package com.nju.edu.community.bl;

import com.nju.edu.community.blservice.CommunityUserBLService;
import com.nju.edu.community.blservice.MessageBLService;
import com.nju.edu.community.blservice.PostBLService;
import com.nju.edu.community.dao.UserDao;
import com.nju.edu.community.entity.NameAndImage;
import com.nju.edu.community.entity.User;
import com.nju.edu.community.vo.postvo.PostListItem;
import com.nju.edu.community.vo.uservo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CommunityUserBL implements CommunityUserBLService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PostBLService postBLService;
    @Autowired
    private MessageBLService messageBLService;

    @Override
    public List<UserInfoVO> searchByKeyword(String keyword) {
        List<UserInfoVO> results=new ArrayList<>();
        List<User> users= userDao.searchByKey(keyword);
        if(users!=null){
            for(User u:users){
                results.add(new UserInfoVO(u));
            }
        }
        return results;
    }

    @Override
    public boolean judgeCollect(String user, String postID) {
        List<String> collects=userDao.findByEmail(user).getCollectPost();
        if (collects==null){
            return false;
        }else return collects.contains(postID);
    }

    @Override
    public boolean judgeStar(String currentUser, String user) {
        for(NameAndImage n:userDao.findByEmail(currentUser).getInterestUser()){
            if (n.getEmail().equals(user)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void interestPost(String username, String postID) {
        Optional<User> o_user= userDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            List<String> collects=user.getCollectPost();
            collects.add(postID);
            user.setCollectPost(collects);
            user.setCollectNum(user.getCollectNum()+1);
            postBLService.addInterestNum(postID);
            userDao.saveAndFlush(user);
        }
    }

    @Override
    public void cancelInterestPost(String username, String postID) {
        User user=userDao.findByEmail(username);
        if (user!=null){
            List<String> collects=user.getCollectPost();
            collects.remove(postID);
            user.setCollectPost(collects);
            user.setCollectNum(user.getCollectNum()-1);
            userDao.saveAndFlush(user);
            postBLService.minusInterestNum(postID);
        }
    }

    @Override
    public void starUser(String fansID, String beStaredID) {
        Optional<User> beStarUser= userDao.findById(beStaredID);
        Optional<User> f_user= userDao.findById(fansID);
        if(beStarUser.isPresent() && f_user.isPresent()){
            //被关注用户的粉丝数+1
            User beStarU=beStarUser.get();
            User fan=f_user.get();

            boolean starEachOther=exist(fansID, beStarU.getInterestUser());

            boolean isFan=exist(fansID,beStarU.getFans());
            if (!isFan){
                beStarU.setFansNum(beStarU.getFansNum()+1);
                //粉丝列表中添加一项
                NameAndImage nameAndImage=new NameAndImage(0,fansID,fan.getNickname(),fan.getImage(),starEachOther);
                beStarU.getFans().add(nameAndImage);
                if (starEachOther){//如果star也关注了fan，那么star的关注列表中fan的状态要变化
                    for (NameAndImage n:beStarU.getInterestUser()){
                        if (n.getEmail().equals(fansID)){
                            n.setFollow(true);
                        }
                    }
                }
                userDao.saveAndFlush(beStarU);
            }

            //粉丝的关注用户数+1
            //粉丝的关注列表添加一项
            boolean haveStared=exist(beStaredID, fan.getInterestUser());
            if (!haveStared){//如果已经关注过
                fan.setInterestNum(fan.getInterestNum()+1);
                NameAndImage nameAndImage1=new NameAndImage(0,beStaredID,beStarU.getNickname(),beStarU.getImage(),starEachOther);
                fan.getInterestUser().add(nameAndImage1);
                messageBLService.generateMessage(beStaredID,fansID+"关注了您");
                userDao.saveAndFlush(fan);
            }


        }
    }

    private boolean exist(String userID, List<NameAndImage> list){
        for (NameAndImage nameAndImage:list){
            if (nameAndImage.getEmail().equals(userID)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void cancelStarUser(String fansID, String starID) {
        Optional<User> fans= userDao.findById(fansID);
        Optional<User> stars= userDao.findById(starID);
        if(fans.isPresent() && stars.isPresent()){
            User fan=fans.get();
            User star=stars.get();

            //取消者的关注列表-1
            fan.setInterestNum(fan.getInterestNum()-1);
            List<NameAndImage> list1=fan.getInterestUser();
            for (NameAndImage nameAndImage:list1){
                if (nameAndImage.getEmail().equals(starID)){
                    list1.remove(nameAndImage);
                    break;
                }
            }
            fan.setInterestUser(list1);
            //如果被取消者是取消者的粉丝，那么取消者的粉丝列表中被取消者的互关状态变为false
            boolean isFan=exist(starID, fan.getFans());
            List<NameAndImage> list=fan.getFans();
            if (isFan){
                for (NameAndImage n:list){
                    if (n.getEmail().equals(starID)){
                        n.setFollow(false);
                    }
                }
            }
            fan.setFans(list);
            userDao.saveAndFlush(fan);

            //被取消者的粉丝数量-1
            star.setFansNum(star.getFansNum()-1);
            List<NameAndImage> list2=star.getFans();
            for (NameAndImage nameAndImage:list2){
                if (nameAndImage.getEmail().equals(fansID)){
                    list2.remove(nameAndImage);
                    break;
                }
            }
            star.setFans(list2);
            userDao.saveAndFlush(fan);
        }
    }

    @Override
    public List<PostListItem> getReleased(String username) {
        return postBLService.getMyRelease(username);
    }

    @Override
    public List<NameAndImage> getInterestedUser(String username) {
        return userDao.findByEmail(username).getInterestUser();
    }

    @Override
    public List<NameAndImage> getFans(String userID) {
        return userDao.findByEmail(userID).getFans();
    }

    @Override
    public void releasePost(String useID) {
        userDao.releasePost(useID);
    }

    @Override
    public List<PostListItem> getCollected(String username) {
        User user=userDao.findByEmail(username);
        if (user!=null){
            ArrayList<String> collectPost=new ArrayList<>(user.getCollectPost());
            return postBLService.getMyCollectPost(collectPost);
        }
        return null;
    }
}
