package me.gaga.springbootkafkabatchprocessing.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessageBuffer {
    private final List<String> messages = new CopyOnWriteArrayList<>();

    public void add(String message) {
        messages.add(message);
    }

    public List<String> getAndClear() {
        List<String> result = new ArrayList<>(messages);
        messages.clear();
        return result;
    }

    public int size() {
        return messages.size();
    }
}