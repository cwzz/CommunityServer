package com.nju.edu.community.bl;

import com.nju.edu.community.blservice.UserBLService;
import com.nju.edu.community.dao.CommunityUserDao;
import com.nju.edu.community.entity.CommunityUser;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserBL implements UserBLService {

    @Autowired
    private CommunityUserDao userDao;

    @Value("${constant.codeLength}")
    private int codeLength;

    @Override
    public ResultMessage getCode(String email){
        if(userDao.existsByEmail(email)>0){
            return ResultMessage.Exist;
        }


        String code=generateCode();
        CommunityUser user =new CommunityUser();
        user.setUserid(email);
        user.setCode(code);
        user.setActive(false);

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
        CommunityUser user=userDao.findByEmail(email);
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

    /*
    @Override
    public ResultMessage checkEmail(String email, String activeCode) {
        Optional<CompanyUser> c_user= companyUserDao.findById(email);
        Optional<PersonalUser> p_user= personalUserDao.findById(email);
        if(c_user.isPresent()){
            CompanyUser user=c_user.get();
            if(user.getActiveCode().equals(activeCode)){
                user.setActive(true);
                companyUserDao.saveAndFlush(user);
                return ResultMessage.Success;
            }
        }else if(p_user.isPresent()){
            PersonalUser personalUser =p_user.get();
            if(personalUser.getActiveCode().equals(activeCode)){
                personalUser.setActive(true);
                personalUserDao.saveAndFlush(personalUser);
                return ResultMessage.Success;
            }
        }
        return ResultMessage.Fail;
    }
    */

    @Override
    public ResultMessage login(String email, String password) {
        CommunityUser user=userDao.findByEmail(email);
        if(user!=null && user.isActive() && user.getPassword().equals(password)){
            return ResultMessage.Success;
        }
        return ResultMessage.Fail;
    }

    @Override
    public ResultMessage forgetPass(String email){
        CommunityUser user=userDao.findByEmail(email);
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
    public ResultMessage resetPassWhenForget(String email, String pass, String code) {
        CommunityUser user=userDao.findByEmail(email);
        if (user.getCode().equals(code)){
            return ResultMessage.CodeError;
        }
        user.setPassword(pass);
        userDao.save(user);
        return ResultMessage.Success;
    }

    @Override
    public String getImageUrl(String username) {

//        String image=null;
//        Optional<PersonalUser> person= personalUserDao.findById(username);
//        Optional<CompanyUser> company= companyUserDao.findById(username);
//        if(person.isPresent()){
//            image=person.get().getImage();
//        }else if(company.isPresent()){
//            image=company.get().getImage();
//        }
//        if(image==null || image.equals("")){
            return "https://ipnet10.oss-cn-beijing.aliyuncs.com/logo.jpg";
//        }
//        return image;
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
