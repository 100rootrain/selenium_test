package com.codingrecipe.member.controller;

import com.codingrecipe.member.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calendar")
@RequiredArgsConstructor

public class CalendarController {
    private final CalendarService calendarService;
    private static final Logger log = LoggerFactory.getLogger(CalendarController.class);
    @GetMapping("/addScheduleForm")
    public String addScheduleForm() {
        return "addScheduleForm";
    }

    @GetMapping("/addHolidayForm")
    public String addHolidayForm() {
        return "addHolidayForm";
    }

    @GetMapping("/holidayChart")
    public String holidayChart() {
        return "holidayChart";
    }

//    @GetMapping("/getCal")
//    public ArrayList<HashMap<String,Object>> getCal(@RequestParam String yearBox, @RequestParam String monthBox, Locale locale, Model model){
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("yearBox",yearBox);
//        map.put("monthBox",monthBox);
//
//        ArrayList<HashMap<String,Object>> CalList = calendarService.getcal(map);
//        return CalList;
//    }



}

