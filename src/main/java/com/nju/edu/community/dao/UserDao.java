package com.nju.edu.community.dao;

import com.nju.edu.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "user")
public interface UserDao extends JpaRepository<User,String> {

    @Query("select count(u) from User u where u.uid=:username and u.active=true ")
    int existsByEmail(@Param("username")String username);

    @Query("select u from User u where u.uid=:username")
    User findByEmail(@Param("username")String email);

    @Modifying
    @Query("update User u set u.password=:pass,u.active=true where u.uid=:id ")
    int activeUser(@Param("id") String email,@Param("pass") String pass);

    @Modifying
    @Query(value = "update User u set u.image=:url where u.uid=:id")
    void updateImageUrl(@Param("id")String userID,@Param("url")String imageUrl);

    @Query(value = "select u from User u where u.nickname like %?1% or u.uid like %?1%")
    List<User> searchByKey(String keyword);

    @Query(value = "update User u set u.releasedNum=u.releasedNum+1 where u.uid=:id")
    @Modifying
    void releasePost(@Param("id") String userId);
}
