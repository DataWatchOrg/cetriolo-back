package dream.team.cetriolo.sprintbootapp.service;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class MessageSender {

    @Value("${rabbit.host}")
    private String rabbitHost;

    private final static String QUEUE_NAME = "spring-boot";

    ConnectionFactory factory = new ConnectionFactory();

    public void send(String message) {
        factory.setHost(rabbitHost);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            System.out.println("RabbitMQ pimbado com sucesso");
        }
    }
}
