package com.codingrecipe.member.service;

import com.codingrecipe.member.repository.AirPollutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirPollutionService {
    private final AirPollutionRepository airPollutionRepository;
    public List<String> getLocationList() {
        List<String> locationList = airPollutionRepository.getLocationList();
        return locationList;
    }

    public List<String> getStationList(String locNm) {
        List<String> stationList =airPollutionRepository.getStationList(locNm);
        return stationList;
    }

}