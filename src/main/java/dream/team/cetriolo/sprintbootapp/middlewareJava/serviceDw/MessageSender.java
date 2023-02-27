package dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.annotation.PreDestroy;
import java.util.logging.Logger;

@Component
public class MessageSender {

    private static final String RABBIT_URI = "amqps://leshjeua:z0mNOy_aGSTEkAR-fLPcUUyc6pwAeTlP@jackal.rmq.cloudamqp.com/leshjeua";
    private static final String QUEUE_NAME = "spring-boot";

    private final ConnectionFactory factory;
    private final ChannelPool channelPool;
    private Connection connection;

    private static final Logger logger = Logger.getLogger(MessageSender.class.getName());

    public MessageSender() throws Exception {
        factory = new ConnectionFactory();
        factory.setUri(RABBIT_URI);
        connection = factory.newConnection();
        channelPool = new ChannelPool(connection, 10);
    }

    public void send(String message) {
        try (Channel channel = channelPool.getChannel()) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (IOException | TimeoutException e) {
            String errorMessage = "Failed to send message: " + e.getMessage();
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        connection.close();
    }
}
