package com.porfiriodev.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null) {
            System.out.println("Usuário já existe!");
            Map<String, String> response = new HashMap<>();
            response.put("message", "O usuário já existe");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        var userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

    }
}
