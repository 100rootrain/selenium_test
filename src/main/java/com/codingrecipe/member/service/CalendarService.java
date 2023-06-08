package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

//    public ArrayList<HashMap<String, Object>> getCal(HashMap<String,Object> map) {
//        ArrayList<HashMap<String,Object>> list = calendarRepository.getCal(map);
//        return list;
//    }

}