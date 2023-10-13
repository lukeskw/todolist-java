package com.porfiriodev.todolist.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {
    public static ResponseEntity<Map<String, String>> defaultResponse(HttpStatus status,String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}
