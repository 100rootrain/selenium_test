package com.codingrecipe.member.repository;

import com.codingrecipe.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CalendarRepository {
    private final SqlSessionTemplate sql;

//    public ArrayList<HashMap<String, Object>> getCal(HashMap<String, Object>map){
//        return sql.selectList("Calendar.getCal",map);
//    };

}
