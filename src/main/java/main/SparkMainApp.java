package main;

import org.apache.log4j.BasicConfigurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.port;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class SparkMainApp {
	public static String controllerPath = "/loginController";
	private static Map<Session, String> users;

	public static void main(String[] args) {
		ProcessBuilder process = new ProcessBuilder();
		users = new ConcurrentHashMap<>();
		int port;
		if (process.environment().get("PORT") != null) {
			port = Integer.parseInt(process.environment().get("PORT"));
		} else {
			port = 4567;
		}
		port(port);

		BasicConfigurator.configure();
		// TODO change this to Spark.staticFiles.location("/public");
		Spark.externalStaticFileLocation("src/main/resources/public");
		SpringApplication.run(SparkMainApp.class, args);

	}

	public static Map<Session, String> getUsers() {
		if(users == null) {
			users = new ConcurrentHashMap<>();
		}
		return users;
	}

	public static void loginUser(Session session, String username) {
		getUsers().put(session, username);
	}

	public static void logoutUser(Session session) {
		getUsers().remove(session);
	}

	public static void broadcastMessage(String receiver) {

	}









}
