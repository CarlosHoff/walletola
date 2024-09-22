package br.com.betola.walletola.message;

import br.com.betola.walletola.domain.PasswordType;
import br.com.betola.walletola.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @KafkaListener(topics = "create-user")
    @Retryable(
            value = {JsonProcessingException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 300000))
    public void listen(String message) throws JsonProcessingException {
        LOG.info(STR."Received message: \{message}");
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(message, User.class);

        User.create(user.email(), PasswordType.PBDKF2, user.password().value());
    }

    @Recover
    public void recover(JsonProcessingException e, String message) {
        //TODO - CRIAR UMA DEAD-LETTER E ENVIAR PARA LÁ

        LOG.info(STR."Message send to Dead-letter: \{message}");
    }

}
