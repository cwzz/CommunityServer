package com.nju.edu.community.bl;

import com.nju.edu.community.blservice.AliService;
import com.nju.edu.community.blservice.UserBLService;
import com.nju.edu.community.dao.UserDao;
import com.nju.edu.community.entity.User;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.enums.Sex;
import com.nju.edu.community.util.MailUtil;
import com.nju.edu.community.vo.uservo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
public class UserBL implements UserBLService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AliService aliService;

    @Value("${constant.codeLength}")
    private int codeLength;

    private static final String defaultImageUrl="https://ipnet10.oss-cn-beijing.aliyuncs.com/ipnet/defaultImg.png";

    @Override
    public ResultMessage getCode(String email){

        if(userDao.existsByEmail(email)>0){
            return ResultMessage.Exist;
        }
        String code=generateCode();
        User user =new User();
        user.setUid(email);
        user.setCode(code);
        user.setActive(false);
        user.setImage(defaultImageUrl);
        user.setSex(Sex.男);
        user.setIntroduce("");
        user.setBirthday("");
        user.setNickname("");
        user.setTags("");

        new Thread( new MailUtil(email, code,true)).start();
//        if(this.sendEmail(email,code)){
            userDao.save(user);
            return ResultMessage.Success;
//        }else{
//            return ResultMessage.Fail;
//        }
    }

    @Override
    @Transactional
    public ResultMessage register(String email,String password,String code) {
        User user=userDao.findByEmail(email);
        if (user==null){
            return ResultMessage.Fail;
        }
        if(user.isActive()){
            return ResultMessage.Exist;
        }
        if(user.getCode().equals(code)){
            //注册成功
            if (userDao.activeUser(email, password)==1){
                return ResultMessage.Success;
            }
        }else{
            return ResultMessage.CodeError;
        }
        return ResultMessage.Fail;
    }

    @Override
    public ResultMessage login(String email, String password) {
        User user=userDao.findByEmail(email);
        if(user!=null && user.isActive() && user.getPassword().equals(password)){
            return ResultMessage.Success;
        }
        return ResultMessage.Fail;
    }

    @Override
    public ResultMessage forgetPass(String email){
        User user=userDao.findByEmail(email);
        if (user==null){
            return ResultMessage.NoUser;
        }
        if (!user.isActive()){
            return ResultMessage.NotActive;
        }
        String newPass=this.generateCode();
        user.setPassword(newPass);
        userDao.save(user);

        new Thread( new MailUtil(email, newPass,false)).start();
        return ResultMessage.Success;
    }

    @Override
    public String getImageUrl(String username) {
        User user=userDao.findByEmail(username);
        if (user==null){
            return defaultImageUrl;
        }
        return user.getImage();
    }

    @Override
    public UserInfoVO getUserInfo(String userID) {
        User user= userDao.findByEmail(userID);
        if(user!=null){
            return new UserInfoVO(user);
        }
        return null;
    }

    @Override
    public ResultMessage  modifyUserInfo(UserInfoVO userInfoVO){
        User user=userDao.findByEmail(userInfoVO.getEmail());
        if (user!=null){
            user.setNickname(userInfoVO.getNickname());
            user.setSex(userInfoVO.getGender());
            user.setBirthday(userInfoVO.getBirth());
            user.setIntroduce(userInfoVO.getDesc());
            StringBuilder tags=new StringBuilder();
            int i=0;
            for (String s:userInfoVO.getInterest()){
                tags.append(s);
                if (i!=userInfoVO.getInterest().length-1){
                    tags.append(",");
                }
                i++;
            }
            user.setTags(tags.toString());
            userDao.save(user);
            return ResultMessage.Success;
        }
        return ResultMessage.Fail;
    }

    @Override
    @Transactional
    public String modifyImage(String userID, MultipartFile file) {
        try{
            String imageUrl="https://"+ aliService.uploadImage(file, userID+file.getOriginalFilename());
            userDao.updateImageUrl(userID, imageUrl);
            return imageUrl;
        }catch (Exception e){
            e.printStackTrace();
            return "FAIL";
        }

    }

    private String generateCode(){
        StringBuilder code= new StringBuilder();
        for(int i=0;i<this.codeLength;i++){
            int r=(int)(Math.random()*10);
            code.append(r);
        }
        return code.toString();
    }

}
