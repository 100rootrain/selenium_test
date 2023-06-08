package com.codingrecipe.member.repository;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AirPollutionRepository {
    private final SqlSessionTemplate sql;

//    public ArrayList<HashMap<String, Object>> getCal(HashMap<String, Object>map){
//        return sql.selectList("Calendar.getCal",map);
//    };
//
    public List<String> getLocationList() {
        return sql.selectList("AirPollution.getLocationList");
    }
//
    public List<String> getStationList(String locNm){
        return sql.selectList("AirPollution.getStationList",locNm);
    };

}
