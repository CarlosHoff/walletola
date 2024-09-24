package br.com.betola.walletola.message;

import br.com.betola.walletola.domain.IdGenerator;
import br.com.betola.walletola.domain.PasswordType;
import br.com.betola.walletola.domain.User;
import br.com.betola.walletola.domain.entity.UserEntity;
import br.com.betola.walletola.domain.request.CreateUserRequest;
import br.com.betola.walletola.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class UserConsumer {
    private static final Logger LOG = LogManager.getLogger(UserConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @KafkaListener(topics = "create-user", groupId = "walletola-group-id")
    @Retryable(
            value = {JsonProcessingException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 300000))
    public void listen(String message) throws JsonProcessingException {
        LOG.info(STR."Received message: \{message}");
        CreateUserRequest request = objectMapper.readValue(message, CreateUserRequest.class);
        User userByMQ = User.create(request.email(), request.passwordType(), request.password());
        userRepository.save(new UserEntity(userByMQ.id(), userByMQ.email(), userByMQ.password().value()));
    }

    @Recover
    public void recover(JsonProcessingException e, String message) {
        //TODO - CRIAR UMA DEAD-LETTER E ENVIAR PARA LÁ

        LOG.info(STR."Message send to Dead-letter: \{message}");
    }

}
