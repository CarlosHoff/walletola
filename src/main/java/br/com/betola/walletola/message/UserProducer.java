package br.com.betola.walletola.message;

import br.com.betola.walletola.domain.request.CreateUserRequest;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    private static final Logger LOG = LogManager.getLogger(UserProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessageForKafka(CreateUserRequest request) {
        try {
            kafkaTemplate.send("create-user", request.toString());
        } catch (TimeoutException e) {
            LOG.error(STR."Timeout Error to send message to kafka: \{e.getMessage()}");
        } catch (KafkaException e) {
            LOG.error(STR."Error to send message to kafka: \{e.getMessage()}");
        }
    }

}
