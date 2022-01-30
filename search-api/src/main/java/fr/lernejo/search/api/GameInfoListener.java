package fr.lernejo.search.api;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GameInfoListener {
    private final RestHighLevelClient Bob;

    public GameInfoListener(RestHighLevelClient restHighlevelClient){
            Bob = restHighlevelClient;
    }

    @RabbitListener(queues = AmqpConfiguration.GAME_INFO_QUEUE)
    public void onMessage (Message mess) throws IOException {

        IndexRequest request = new IndexRequest("games", mess.getMessageProperties().getHeader("game_id") );
        request.source(mess.getBody(), XContentType.JSON);
        Bob.index(request, RequestOptions.DEFAULT);
    }

}
