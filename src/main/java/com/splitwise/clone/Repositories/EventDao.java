package com.splitwise.clone.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitwise.clone.Entities.Event;

public interface EventDao extends JpaRepository<Event, Integer> {
    
}
