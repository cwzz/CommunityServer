package com.nju.edu.community.blservice;

import com.nju.edu.community.entity.CommunityMessage;
import com.nju.edu.community.vo.MessageSetRead;

import java.util.ArrayList;

public interface CommunityMessageBLService {

    void generateMessage(String receiver, String event);

    void generateMessage(ArrayList<String> receiver, String event);

    void setRead(MessageSetRead messageSetRead);

    ArrayList<CommunityMessage> getAllMessageList(String username);

    ArrayList<CommunityMessage> getUnreadMessageList(String username);
}
