package br.edu.atitus.luis_canal.apisample.controllers;

import br.edu.atitus.luis_canal.apisample.dtos.SignupDTO;
import br.edu.atitus.luis_canal.apisample.entities.User;
import br.edu.atitus.luis_canal.apisample.entities.UserType;
import br.edu.atitus.luis_canal.apisample.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> postSignup(@RequestBody SignupDTO dto) throws Exception{
        User newUser = new User();
        BeanUtils.copyProperties(dto, newUser);
        newUser.setType(UserType.Common);
        userService.save(newUser);

        return ResponseEntity.ok(newUser);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        String message = ex.getMessage().replace("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }
}
