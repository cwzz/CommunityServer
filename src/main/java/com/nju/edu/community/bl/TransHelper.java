package com.nju.edu.community.bl;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

@Service
public class TransHelper {

    private static Mapper mapper = new DozerBeanMapper();

    public Object transTO(Object src , Class dest){
        return mapper.map(src,dest);
    }

}
