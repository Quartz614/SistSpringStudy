package com.sist.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.mapper.*;
import com.sist.vo.*;

@Repository //메모리 할당 요청 
public class NatureDAO {
    
    // MyBatis에서 구현된 객체 주소값을 주입 요청 
    @Autowired
    private NatureMapper mapper;
    
    public List<NatureVO> natureListData(Map map) {
        return mapper.natureListData(map);
    }

    public int natureTotalPage() {
        return mapper.natureTotalPage();
    }

    public NatureVO natureDetailData(int no) {
        return mapper.natureDetailData(no);
    }
}