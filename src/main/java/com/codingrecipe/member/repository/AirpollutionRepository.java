package com.codingrecipe.member.repository;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
public class AirpollutionRepository {
    private final SqlSessionTemplate sql;

//    public ArrayList<HashMap<String, Object>> getCal(HashMap<String, Object>map){
//        return sql.selectList("Calendar.getCal",map);
//    };
//
//    public ArrayList<HashMap<String, Object>> getLocationList() {
//        return sql.selectMap("AirPollution.getLocationList")
//    }
//
//    public ArrayList<HashMap<String, Object>> getStationList(HashMap<String, Object> map);

}
