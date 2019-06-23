package com.nju.edu.community.dao;

import com.nju.edu.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.ArrayList;

@Repository
@Table(name = "post")
public interface PostDao extends JpaRepository<Post,String> {
    @Query("select p from Post p where p.author=:author")
    ArrayList<Post> getPostByAuthor(@Param("author") String author);

    @Query(value = "select p from Post p where p.title like %?1%")
    ArrayList<Post> searchArticle(String keywords);

    @Query(value = "select p from Post p where p.category=:category and p.postTag=:tag and p.state='Published' order by p.visits desc")
    ArrayList<Post> searchArticleByTag(@Param("category")String category, @Param("tag")String tag);

    @Query(value = "select p from Post p where p.category=:category and p.state='Published' order by p.visits desc " )
    ArrayList<Post> searchArticleByCategory(@Param("category")String category);

    @Query(value = "select p from Post p where p.state='Published' order by p.visits desc ")
    ArrayList<Post> searchAllArticle();
}
