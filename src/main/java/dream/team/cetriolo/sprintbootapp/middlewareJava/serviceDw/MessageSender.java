package dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.*;

@Component
public class MessageSender {

    private String rabbitUri = "amqps://hhzfkiof:H7a48WnpOQqZdu8evXX30WSlQFMAyCU0@jackal.rmq.cloudamqp.com/hhzfkiof";

    private final static String QUEUE_NAME = "spring-boot";

    ConnectionFactory factory = new ConnectionFactory();

    public void send(String message) {
        try {
            factory.setUri(rabbitUri);
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
