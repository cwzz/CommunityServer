package com.nju.edu.community.dao;

import com.nju.edu.community.entity.User;
import com.nju.edu.community.entity.Mine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "com_user")
public interface CommunityUserDao extends JpaRepository<User,String> {

    @Query("select count(c) from User c where c.getUid=:username")
    int existsByEmail(@Param("username")String username);

    @Query("select u from User u where u.getUid=:username")
    User findByEmail(@Param("username")String email);

    @Modifying
    @Query("update User u set u.password=:pass,u.active=true where u.getUid=:id ")
    int activeUser(@Param("id") String email,@Param("pass") String pass);

    @Modifying
    @Query(value = "update User c set c.nickname=:nickname where c.getUid=:username")
    void modifySignature(@Param("username") String username, @Param("nickname") String nickname);

    @Modifying
    @Query(value = "update User c set c.tags=:newTag where c.getUid=:username")
    void modifyTag(@Param("username") String username, @Param("newTag") String newTag);

    @Query(value = "select c.mines from User c where c.getUid=:username")
    List<Mine> getMine(@Param("username") String username);

    @Query(value = "select c from User c where c.nickname like %?1% or c.getUid like %?1%")
    List<User> searchByKey(String keyword);
}
