package com.nju.edu.community.dao;

import com.nju.edu.community.entity.Remark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RemarkDao extends JpaRepository<Remark, Long> {

    @Query(value = "select r from Remark r where r.post_id=:pid order by r.remark_time desc")
    ArrayList<Remark> getRemarksOrderByTime(@Param("pid")String pid);
}
