package org.cs304.backend.controller;

import org.cs304.backend.entity.Event;
import org.cs304.backend.service.impl.EventServiceImpl;
import org.cs304.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventServiceImpl eventService;

    @PostMapping("/add")
    Result postNewEvent(@RequestParam Event event){
        eventService.insertEvent(event);
        return new Result();
    }

}
