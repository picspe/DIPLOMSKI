package main.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.dao.MessageRepository;
import main.dao.UserRepository;

import static spark.Spark.*;

import main.models.Message;
import main.models.User;
import main.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Controller
public class UserController {
    public static String controllerPath = "/user";
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;

    public UserController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.objectMapper = new ObjectMapper();
        this.registerRoutes();
    }

    private void registerRoutes() {
        post(controllerPath + "/login", (request, response) -> {
            User login = objectMapper.mapFromJson(request.body(), User.class);
            if (login == null) {
                response.status(404);
                return false;
            }
            User returnUser = userRepository.findOneByEmail(login.getEmail());
            if (login.getPassword().equals(returnUser.getPassword())) {
                request.session().attribute("user", returnUser.getEmail());
                return objectMapper.mapToJson(returnUser);
            } else {
                response.status(404);
                return false;
            }
        });

        post(controllerPath + "/register", (request, response) -> {
            User user = objectMapper.mapFromJson(request.body(), User.class);
            User existing = userRepository.findOneByEmail(user.getEmail());
            if(existing == null)
                return userRepository.save(user);
            else {
                response.status(400);
                return "Username already in use.";
            }
        });

        get(controllerPath + "/logout", (request, response) -> {
            request.session().removeAttribute("user");
            return true;
        });

        get(controllerPath + "/isLoggedIn", (request, response) -> {
            String user = request.session().attribute("user");

            if (user == null) {
                response.status(404);
                return "";
            }
            else
                return user;
        });
        get(controllerPath + "/search/:username", (request, response) -> {
            String username = request.params("username");
            Collection<User> users = userRepository.findTop10ByEmailContaining(username);
            return objectMapper.mapToJson(users);
        });

        post(controllerPath + "/send", (request, response) -> {
            Message newMessage = objectMapper.mapFromJson(request.body(), Message.class);
            newMessage.setId(null);
            if(newMessage.getReceiverMail() == null || newMessage.getReceiverMail().isEmpty()) {
                response.status(404);
                return "User not found";
            } else if (newMessage.getSubject() == null || newMessage.getSubject().isEmpty()) {
                response.status(400);
                return "Please enter subject";
            } else if (newMessage.getMessage() == null || newMessage.getMessage().isEmpty()) {
                response.status(400);
                return "Please enter message";
            }
            String receiver = newMessage.getReceiverMail();
            if(!userRepository.existsByEmail(receiver)) {
                response.status(404);
                return "User not found.";
            }

            if (newMessage != null) {
                String sender = request.session().attribute("user");
                if(sender == null) {
                    response.status(404);
                    return "Your session has expired, please log in again";
                }
                newMessage.setSenderMail(sender);
                newMessage.setDate(new Timestamp(new Date().getTime()));
                newMessage.setSeen(false);
                messageRepository.save(newMessage);
                if(newMessage.getCc() != null) {
                    String[] ccs = newMessage.getCc().split(",");
                    for(int i = 0; i < ccs.length; i++) {
                        Message ccMessage = objectMapper.copy(newMessage, Message.class);
                        if(!ccs[i].replace(" ", "").isEmpty()) {
                            if(!userRepository.existsByEmail(ccs[i])) {
                                response.status(404);
                                return "User not found.";
                            }
                            ccMessage.setReceiverMail(ccs[i]);
                            messageRepository.save(ccMessage);
                        }
                    }
                }
                response.status(200);
                return objectMapper.mapToJson(newMessage);
            } else {
                response.status(400);
                return "Error";
            }
        });
    }
}
