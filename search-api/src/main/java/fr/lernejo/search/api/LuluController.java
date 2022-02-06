package fr.lernejo.search.api;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.List;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class LuluController {
    private final RestHighLevelClient Patrick;

    public LuluController(RestHighLevelClient restHighlevelClient){
        Patrick = restHighlevelClient;
    }


    @GetMapping("/api/games")
    public ArrayList<Map<String, Object>> endpoint(@RequestParam String query){
        SearchRequest searchRequest = new SearchRequest("games");
        searchRequest.source().query(QueryBuilders.queryStringQuery(query));
        ArrayList<Map<String, Object>> arrayList = new  ArrayList<>();
        try {
            SearchResponse searchResponse = Patrick.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits temp = searchResponse.getHits();

            for (SearchHit item: temp
                 ) {
                arrayList.add(item.getSourceAsMap());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  arrayList;
    }
}
