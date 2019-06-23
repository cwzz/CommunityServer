package com.nju.edu.community.dao;

import com.nju.edu.community.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.ArrayList;

@Repository
@Table(name = "com_message")
public interface MessageDao extends JpaRepository<Message,Long> {

    @Query(value = "select m from Message m where m.receiver=:username and m.time=:t")
    Message findByUserAndTime(@Param(value = "username") String username, @Param(value = "t") String time);

    @Query(value = "select m from Message m where m.receiver=:username order by m.time desc")
    ArrayList<Message> findAllSortByTime(@Param(value = "username") String username);

    @Query(value = "select m from Message m where m.receiver=:username and m.isRead=false order by m.time desc")
    ArrayList<Message> findUnreadSortByTime(@Param(value = "username") String username);
}
