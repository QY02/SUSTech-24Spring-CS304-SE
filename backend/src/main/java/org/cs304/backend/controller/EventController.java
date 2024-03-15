package org.cs304.backend.controller;

import jakarta.annotation.Resource;
import org.cs304.backend.entity.Event;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    @Resource
    private IEventService eventService;

    @PostMapping("/add")
    Result postNewEvent(@RequestParam Event event){
        eventService.save(event);
        return new Result();
    }

}
