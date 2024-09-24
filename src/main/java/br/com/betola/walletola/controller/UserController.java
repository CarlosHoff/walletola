package br.com.betola.walletola.controller;

import br.com.betola.walletola.domain.User;
import br.com.betola.walletola.domain.request.CreateUserRequest;
import br.com.betola.walletola.domain.request.RestoreUserRequest;
import br.com.betola.walletola.domain.response.CreateUserResponse;
import br.com.betola.walletola.domain.response.RestoreUserResponse;
import br.com.betola.walletola.domain.response.UserResponse;
import br.com.betola.walletola.message.UserProducer;
import br.com.betola.walletola.usecases.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserProducer userProducer;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request,
                                                     @RequestParam(name = "latency", defaultValue = "10", required = false) int latency) throws InterruptedException, JsonProcessingException {
        LOG.info("UserController - POST /create");
        Thread.sleep(latency);
        User user = null;
        try {
            user = userService.create(request);
            CreateUserResponse response = new CreateUserResponse(user.id());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            userProducer.sendMessageForKafka(request);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/create/mq")
    public void createByMQ(@RequestBody CreateUserRequest request) throws JsonProcessingException {
        LOG.info("UserController - POST /create/mq");
        userProducer.sendMessageForKafka(request);
    }

    @PostMapping("/restore")
    public ResponseEntity<RestoreUserResponse> restore(@RequestBody RestoreUserRequest request) {
        LOG.info("UserController - POST /restore");
        try {
            User user = userService.restore(request);
            RestoreUserResponse response = new RestoreUserResponse(user.password().value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findUserByID")
    public ResponseEntity<UserResponse> getUserByID(@RequestParam String userID) {
        LOG.info("UserController - GET /findUserByID");
        try {
            UserResponse user = userService.getUserByID(userID);
            return new ResponseEntity<>(new UserResponse(user.id(), user.email(), user.password()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findUserByEmail")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        LOG.info("UserController - GET /findUserByEmail");
        try {
            UserResponse user = userService.getUserByEmail(email);
            return new ResponseEntity<>(new UserResponse(user.id(), user.email(), user.password()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
