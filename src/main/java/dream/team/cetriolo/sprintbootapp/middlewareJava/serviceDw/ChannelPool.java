package dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ChannelPool {

    private final Connection connection;
    private final int poolSize;
    private final List<Channel> channels;
    private final Object lock = new Object();

    public ChannelPool(Connection connection, int poolSize) {
        this.connection = connection;
        this.poolSize = poolSize;
        this.channels = new ArrayList<>(poolSize);
    }

    public Channel getChannel() throws IOException {
        synchronized (lock) {
            if (channels.isEmpty()) {
                return createChannel();
            } else {
                return channels.remove(channels.size() - 1);
            }
        }
    }

    public void releaseChannel(Channel channel) {
        synchronized (lock) {
            if (channels.size() < poolSize) {
                channels.add(channel);
            } else {
                closeChannel(channel);
            }
        }
    }

    private Channel createChannel() throws IOException {
        return connection.createChannel();
    }

    private void closeChannel(Channel channel) {
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            // ignore
        }
    }
}