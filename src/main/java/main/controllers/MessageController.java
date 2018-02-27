package main.controllers;

import static spark.Spark.*;

import main.dao.MessageRepository;
import main.models.Message;
import main.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class MessageController {
    public static String controllerPath = "/message";
    private ObjectMapper objectMapper;

    @Autowired
    MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.objectMapper = new ObjectMapper();
        this.setupRoutes();
    }

    private void setupRoutes(){
        post(controllerPath + "/send", (request, response) -> {
            Message newMessage = objectMapper.mapFromJson(request.body(), Message.class);
            if (newMessage != null) {
                messageRepository.save(newMessage);
                response.status(200);
                return objectMapper.mapToJson(newMessage);
            } else {
                response.status(400);
                return "Error";
            }
        });

        get(controllerPath + "/inbox", (request, response) -> {
            String user = request.session().attribute("user");
            if(user != null) {
                Collection<Message> inbox = messageRepository.findAllByReceiverMail(user);
                return objectMapper.mapToJson(inbox);
            } else {
                response.status(400);
                return "Please sign in!";
            }
        });

        get(controllerPath + "/outbox", (request, response) -> {
            String user = request.session().attribute("user");
            if(user != null) {
                Collection<Message> outbox = messageRepository.findAllBySenderMail(user);
                return outbox;
            } else {
                response.status(400);
                return "Please sign in!";
            }
        });
    }
}
