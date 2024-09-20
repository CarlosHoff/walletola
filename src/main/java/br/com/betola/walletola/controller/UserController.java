package br.com.betola.walletola.controller;

import br.com.betola.walletola.domain.User;
import br.com.betola.walletola.domain.request.CreateUserRequest;
import br.com.betola.walletola.domain.request.RestoreUserRequest;
import br.com.betola.walletola.domain.response.CreateUserResponse;
import br.com.betola.walletola.domain.response.RestoreUserResponse;
import br.com.betola.walletola.domain.response.UserResponse;
import br.com.betola.walletola.usecases.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.create(request);
            CreateUserResponse response = new CreateUserResponse(user.id());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<RestoreUserResponse> restore(@RequestBody RestoreUserRequest request) {
        try {
            User user = userService.restore(request);
            RestoreUserResponse response = new RestoreUserResponse(user.password().value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findUserByID/{id}")
    public ResponseEntity<UserResponse> getUserByID(@RequestParam String userID) {
        try {
            UserResponse user = userService.getUserByID(userID);
            return new ResponseEntity<>(new UserResponse(user.id(), user.email(), user.password()), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findUserByID/{id}")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        try {
            UserResponse user = userService.getUserByEmail(email);
            return new ResponseEntity<>(new UserResponse(user.id(), user.email(), user.password()), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
