package com.nju.edu.community.dao;

import com.nju.edu.community.entity.CommunityUser;
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
public interface CommunityUserDao extends JpaRepository<CommunityUser,String> {

    @Query("select count(c) from CommunityUser c where c.userid=:username")
    int existsByEmail(@Param("username")String username);

    @Query("select u from CommunityUser u where u.userid=:username")
    CommunityUser findByEmail(@Param("username")String email);

    @Modifying
    @Query("update CommunityUser u set u.password=:pass,u.active=true where u.userid=:id ")
    int activeUser(@Param("id") String email,@Param("pass") String pass);

    @Modifying
    @Query(value = "update CommunityUser c set c.nickname=:nickname where c.userid=:username")
    void modifySignature(@Param("username") String username, @Param("nickname") String nickname);

    @Modifying
    @Query(value = "update CommunityUser c set c.tags=:newTag where c.userid=:username")
    void modifyTag(@Param("username") String username, @Param("newTag") String newTag);

    @Query(value = "select c.mines from CommunityUser c where c.userid=:username")
    List<Mine> getMine(@Param("username") String username);

    @Query(value = "select c from CommunityUser c where c.nickname like %?1% or c.userid like %?1%")
    List<CommunityUser> searchByKey(String keyword);
}
