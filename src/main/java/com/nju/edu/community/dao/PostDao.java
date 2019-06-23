package com.nju.edu.community.dao;

import com.nju.edu.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.ArrayList;

@Repository
@Table(name = "post")
public interface PostDao extends JpaRepository<Post,String> {
    @Query("select p from Post p where p.author=:author and p.state='Published' order by p.publishTime desc ")
    ArrayList<Post> getPostByAuthor(@Param("author") String author);

    @Query(value = "select p from Post p where p.title like %?1%")
    ArrayList<Post> searchArticle(String keywords);

    @Query(value = "select p from Post p where p.category=:category and p.postTag=:tag and p.state=1 order by p.visits desc")
    ArrayList<Post> searchArticleByTag(@Param("category")String category, @Param("tag")String tag);

    @Query(value = "select p from Post p where p.category=:category and p.state=1 order by p.visits desc " )
    ArrayList<Post> searchArticleByCategory(@Param("category")String category);

    @Query(value = "select p from Post p where p.state=1 order by p.visits desc ")
    ArrayList<Post> searchAllArticle();

    @Query(value = "update Post p set p.interestNum=p.interestNum+1 where p.pid=:id")
    @Modifying
    void addInterestNum(@Param("id")String id);

    @Query(value = "update Post p set p.interestNum=p.interestNum-1 where p.pid=:id")
    @Modifying
    void minusInterestNum(@Param("id")String id);
}
