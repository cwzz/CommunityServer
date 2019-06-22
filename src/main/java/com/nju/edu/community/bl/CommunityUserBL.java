package com.nju.edu.community.bl;

import com.nju.edu.community.blservice.CommunityUserBLService;
import com.nju.edu.community.blservice.PostBLService;
import com.nju.edu.community.blservice.UserBLService;
import com.nju.edu.community.dao.CommunityUserDao;
import com.nju.edu.community.entity.User;
import com.nju.edu.community.entity.Mine;
import com.nju.edu.community.entity.MineTag;
import com.nju.edu.community.entity.Record;
import com.nju.edu.community.enums.PostTag;
import com.nju.edu.community.vo.BriefPost;
import com.nju.edu.community.vo.BriefUser;
import com.nju.edu.community.vo.CUserVO;
import com.nju.edu.community.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CommunityUserBL implements CommunityUserBLService {

    @Autowired
    private CommunityUserDao communityUserDao;
    @Autowired
    private TransHelper transHelper;
    @Autowired
    private PostBLService postBLService;
    @Autowired
    private UserBLService userBLService;

    @Override
    public List<BriefUser> searchByKeyword(String keyword) {
        List<BriefUser> results=new ArrayList<>();
        List<User> users=communityUserDao.searchByKey(keyword);
        if(users!=null){
            for(User u:users){
                BriefUser briefUser=(BriefUser)transHelper.transTO(u,BriefUser.class);
                briefUser.setUrl(userBLService.getImageUrl(u.getUid()));
                results.add(briefUser);
            }
        }
        return results;
    }

    @Override
    public CUserVO getUserInfo(String userID) {
        Optional<User> user=communityUserDao.findById(userID);
        if(user.isPresent()){
            CUserVO cUserVO=(CUserVO) this.transHelper.transTO(user.get(),CUserVO.class);
            cUserVO.setMyTags(user.get().getTags().split(","));
            cUserVO.setUrl(userBLService.getImageUrl(userID));
            return cUserVO;
        }
        return null;
    }

    @Override
    public void modifyNickname(String username, String signature) {
        communityUserDao.modifySignature(username,signature);
    }

    @Override
    public void modifyTag(String username, ArrayList<PostTag> tags) {
        StringBuilder newSign= new StringBuilder();
        for(PostTag tag:tags){
            newSign.append(tag.toString());
        }
        communityUserDao.modifyTag(username, newSign.toString());
    }

    @Override
    public void releasePost(String username, String postID) {
        Optional<User> o_user=communityUserDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            user.setReleasedNum(user.getReleasedNum()+1);
            user.setMines(this.addMine(user.getMines(),postID,MineTag.Release));
            communityUserDao.saveAndFlush(user);
        }
    }

    @Override
    public void interestPost(String username, String postID) {
        Optional<User> o_user=communityUserDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            user.setInterestNum(user.getInterestNum()+1);
            user.setMines(this.addMine(user.getMines(),postID,MineTag.InterestPost));
            postBLService.addInterestNum(postID);
            communityUserDao.saveAndFlush(user);
        }
    }

    /*@Override
    public void collectPost(String username, String postID) {
        Optional<User> o_user=communityUserDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            user.setCollectNum(user.getCollectNum()+1);
            user.setMines(this.addMine(user.getMines(),postID,MineTag.Collect));
            communityUserDao.saveAndFlush(user);
        }
    }*/

    @Override
    public void interestUser(String starID, String fanID) {
        Optional<User> s_user=communityUserDao.findById(starID);
        Optional<User> f_user=communityUserDao.findById(fanID);
        if(s_user.isPresent() && f_user.isPresent()){
            //被关注用户的粉丝数+1
            User star=s_user.get();
            star.setFansNum(star.getFansNum()+1);
            star.setMines(this.addMine(star.getMines(),fanID, MineTag.Fan));
            communityUserDao.saveAndFlush(star);

            //粉丝的关注用户数+1
            User fan=f_user.get();
            fan.setInterestNum(fan.getInterestNum()+1);
            fan.setMines(this.addMine(fan.getMines(),starID,MineTag.InterestUser));
            communityUserDao.saveAndFlush(fan);
        }
    }

    private List<Mine> addMine(List<Mine> mines, String toAddId, MineTag tag){
        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Mine newMine=new Mine(0,toAddId,time,tag);
        mines.add(newMine);
        return mines;
    }

    @Override
    public void deletePost(String username, String postID) {
        Optional<User> o_user=communityUserDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            user.setReleasedNum(user.getReleasedNum()-1);
            user.setMines(this.removeMine(user.getMines(),postID,MineTag.Release));
            communityUserDao.saveAndFlush(user);
        }
    }

    @Override
    public void cancelInterest(String username, String postID) {
        Optional<User> o_user=communityUserDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            user.setInterestNum(user.getInterestNum()-1);
            user.setMines(this.removeMine(user.getMines(),postID,MineTag.InterestPost));
            postBLService.minusInterestNum(postID);
            communityUserDao.saveAndFlush(user);
        }
    }

    /*@Override
    public void cancelCollect(String username, String postID) {
        Optional<User> o_user=communityUserDao.findById(username);
        if(o_user.isPresent()){
            User user=o_user.get();
            user.setCollectNum(user.getCollectNum()-1);
            user.setMines(this.removeMine(user.getMines(),postID,MineTag.Collect));
            communityUserDao.saveAndFlush(user);
        }
    }*/

    @Override
    public void cancelInterestUser(String starID, String fanID) {
        Optional<User> s_user=communityUserDao.findById(starID);
        Optional<User> f_user=communityUserDao.findById(fanID);
        if(s_user.isPresent() && f_user.isPresent()){
            //被取关用户的粉丝数-1
            User star=s_user.get();
            star.setFansNum(star.getFansNum()-1);
            star.setMines(this.removeMine(star.getMines(),fanID,MineTag.Fan));
            communityUserDao.saveAndFlush(star);
            //取消关注的用户关注用户列表-1
            User fan=f_user.get();
            fan.setInterestNum(fan.getInterestNum()-1);
            fan.setMines(this.removeMine(fan.getMines(),starID,MineTag.InterestUser));
            communityUserDao.saveAndFlush(fan);
        }
    }

    @Override
    public void browsePost(String userID, String postID,String postname) {
        Optional<User> o_user=communityUserDao.findById(userID);
        boolean isExist=false;
        if(o_user.isPresent()){
            User user=o_user.get();
            List<Record> records=user.getHistory();
            for(Record record:records){
                if(record.getPostid().equals(postID)){//浏览记录中已经存在的
                    isExist=true;
                    record.setTime(new Date());
                    break;
                }
            }
            if (!isExist) {
                if(records.size()>=7){
                    //如果记录超过7条，找到最早的，移除
                    int index=0;
                    for(int i=1;i<records.size();i++){
                        if(records.get(i).getTime().before(records.get(index).getTime())){
                            index=i;
                        }
                    }
                    records.remove(index);
                }
                Record newRecord=new Record(0,postID,postname,new Date());
                records.add(newRecord);
            }
            user.setHistory(records);
            communityUserDao.saveAndFlush(user);
        }
    }

    private List<Mine> removeMine(List<Mine> mines,String toRemoveId,MineTag tag){
        for(Mine mine:mines){
            if(mine.getTag()==tag && mine.getTid().equals(toRemoveId)){
                mines.remove(mine);
                break;
            }
        }
        return mines;
    }

    @Override
    public List<BriefPost> getReleased(String username) {
        List<BriefPost> posts=new ArrayList<>();
        List<Mine> mines=this.getMine(username,MineTag.Release);
        for(Mine mine:mines){
            posts.add(postBLService.getBriefPostByID(mine.getTid()));
        }
        return posts;
    }

    @Override
    public List<BriefPost> getInterestedpost(String username) {
        List<BriefPost> posts=new ArrayList<>();
        List<Mine> mines=this.getMine(username,MineTag.InterestPost);
        for(Mine mine:mines){
            posts.add(postBLService.getBriefPostByID(mine.getTid()));
        }
        return posts;
    }

    /*@Override
    public List<Mine> getCollected(String username) {
        return this.getMine(username,MineTag.Collect);
    }*/

    @Override
    public List<BriefUser> getInterestedUser(String username) {
        List<Mine> mines=this.getMine(username,MineTag.InterestUser);
        List<BriefUser> users=new ArrayList<>();
        for(Mine mine:mines){
            Optional<User> user=communityUserDao.findById(mine.getTid());
            if(user.isPresent()){
                BriefUser briefUser=(BriefUser) this.transHelper.transTO(user.get(),BriefUser.class);
                briefUser.setUrl(userBLService.getImageUrl(mine.getTid()));
                users.add(briefUser);
            }
        }
        return users;
    }

    private List<Mine> getMine(String username,MineTag tag){
        List<Mine> result=new ArrayList<>();
        for(Mine mine:communityUserDao.getMine(username)){
            if(mine.getTag()==tag){
                result.add(mine);
            }
        }
        return result;
    }

    @Override
    public List<RecordVO> getHistory(String userID){
        Optional<User> o_user=communityUserDao.findById(userID);
        List<RecordVO> results=new ArrayList<>();
        if(o_user.isPresent()){
            List<Record> records=o_user.get().getHistory();
            for(Record record:records){
                System.err.println(record.toString());
                RecordVO temp=(RecordVO) transHelper.transTO(record,RecordVO.class);
                temp.setUrl(userBLService.getImageUrl(record.getPostid().substring(14)));
//                temp.setUrl(userBLService.getImageUrl(userID));
                results.add(temp);
            }
            return results;
        }
        return null;
    }

    @Override
    public List<Mine> getCollected(String username) {
        return null;
    }
}
