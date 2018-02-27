package main.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.dao.UserRepository;

import static spark.Spark.*;

import main.models.User;
import main.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;

@Controller
public class UserController {
    public static String controllerPath = "/user";
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    }
}
