package fr.lernejo.fileinjector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.MimeTypeUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            return;

            try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
                File fich = new File(args[0]);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<Game> jeu = objectMapper.readValue(fich, new TypeReference<List<Game>>() {
                    });

                    RabbitTemplate listMess = springContext.getBean(RabbitTemplate.class);
                    listMess.setMessageConverter(new Jackson2JsonMessageConverter());
                    for (Game item : jeu
                    ) {
                        listMess.convertAndSend("game_info", item, message -> {
                            message.getMessageProperties().getHeaders().put("game_id", item.id().toString());
                            return message;
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Hello after starting Spring");
            }

        }

    }

