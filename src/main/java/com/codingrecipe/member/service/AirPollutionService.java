package com.codingrecipe.member.service;

import com.codingrecipe.member.repository.AirpollutionRepository;
import com.codingrecipe.member.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AirPollutionService {
    private final AirpollutionRepository airpollutionRepository;
//    public ArrayList<HashMap<String, Object>> getLocationList() {
//        ArrayList<HashMap<String, Object>> list = airpollutionRepository.getLocationList();
//        return list;
//    }
//
//    public ArrayList<HashMap<String, Object>> getStationList(HashMap<String, Object> map) {
//        return airpollutionRepository.getStationList(map);
//    }

}