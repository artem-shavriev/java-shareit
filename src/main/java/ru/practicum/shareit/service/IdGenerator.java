package ru.practicum.shareit.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdGenerator {

    public Integer getNextId(Map data) {
        Integer currentMaxId = data.keySet()
                .stream()
                .mapToInt(id -> (Integer) id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}