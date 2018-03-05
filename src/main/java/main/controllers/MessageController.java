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
                return objectMapper.mapToJson(outbox);
            } else {
                response.status(400);
                return "Please sign in!";
            }
        });

        post( controllerPath + "/seen" , ((request, response) -> {
            Message message = objectMapper.mapFromJson(request.body(), Message.class);
            message.setSeen(true);
            messageRepository.save(message);
            return "Seen";
        }));

        post( controllerPath + "/delete" , ((request, response) -> {
            Message message = objectMapper.mapFromJson(request.body(), Message.class);
            messageRepository.delete(message.getId());
            return "Ok";
        }));

    }
}
