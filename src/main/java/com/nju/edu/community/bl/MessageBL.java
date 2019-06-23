package com.nju.edu.community.bl;

import com.nju.edu.community.blservice.MessageBLService;
import com.nju.edu.community.dao.MessageDao;
import com.nju.edu.community.entity.Message;
import com.nju.edu.community.vo.MessageSetRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageBL implements MessageBLService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public void generateMessage(String receiver, String event) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(new Date());
        Message message=new Message(0,receiver,event,time,false);
        messageDao.saveAndFlush(message);
    }

    @Override
    public void generateMessage(ArrayList<String> receiver, String event) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(new Date());
        List<Message> messages=new ArrayList<>();
        for(String s:receiver){
            Message message=new Message(0,s, event,time,false);
            messages.add(message);
        }
        messageDao.saveAll(messages);
    }

    @Override
    public void setRead(MessageSetRead messageSetRead) {
        String username=messageSetRead.getUsername();
        for(String time:messageSetRead.getMessageTimeList()){
            Message message= messageDao.findByUserAndTime(username,time);
            message.setRead(true);
            messageDao.saveAndFlush(message);
        }
    }

    @Override
    public ArrayList<Message> getAllMessageList(String username) {
        return messageDao.findAllSortByTime(username);
    }

    @Override
    public ArrayList<Message> getUnreadMessageList(String username) {
        return messageDao.findUnreadSortByTime(username);
    }
}
