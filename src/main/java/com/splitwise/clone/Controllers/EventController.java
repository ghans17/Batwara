package com.splitwise.clone.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitwise.clone.Entities.Event;
import com.splitwise.clone.Entities.User;
import com.splitwise.clone.Repositories.EventDao;
import com.splitwise.clone.Repositories.UserDao;
import com.splitwise.clone.Services.EventService;

import lombok.Data;
import java.util.*;

@RestController
@Data
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;
    @Autowired
    UserDao userDao;
    @Autowired
    EventDao eventDao;

    @GetMapping("geteventsbyuser/{userId}")
    public ResponseEntity<Set<Event>> getEventsByUser(@PathVariable("userId") int userId) {
      if (userDao.findById(userId)!= null) {
        return new ResponseEntity<>(eventService.getAllEventsByUser(userId),HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }

    @PostMapping("/addevent")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.addEvent(event), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") int eventId){
        try{
            return new ResponseEntity<>(eventService.deleteEvent(eventId),HttpStatus.GONE);
        }catch(Exception e){
        return new ResponseEntity("Server error occured.", HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable("eventId") int eventId, @RequestBody Event event) {
        if (eventDao.findById(eventId)!=null) {
            return new ResponseEntity<>(eventService.updateEvent(eventId , event),HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/geteventmembers/{eventId}")
    public ResponseEntity<Set<User>> getEventMembers(@PathVariable("eventId") int eventId) {
        if (eventDao.findById(eventId)!=null) {
            return new ResponseEntity<>(eventService.getEventMembers(eventId),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/addmember/{eventId}/{userName}")
    public ResponseEntity<Event> addMember(@PathVariable("eventId") int eventId, @PathVariable("userName") String userName) {
        if (eventDao.findById(eventId)!=null && userDao.findByUserName(userName) !=null) {
            return new ResponseEntity<>(eventService.addMember(userName, eventId),HttpStatus.CREATED);
        }else{
            return new ResponseEntity("Server error occured",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
