package package1;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class EsOperation {
	private static final EsOperation ES_OPERATION=new EsOperation();

	public static EsOperation getInstance() {
		return ES_OPERATION;
	}

	public boolean indexExist(String indexName) {
		boolean exist;
		RestHighLevelClient client=new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http")));
		GetIndexRequest getIndexRequest=new GetIndexRequest(indexName);
		try {
			exist=client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			exist=false;
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exist;
	}

	public HashMap<String, Object> insertData(String indexName, HashMap<String, Object> map) {
		RestHighLevelClient client=new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http")));
		
		IndexRequest indexRequest=new IndexRequest(indexName);
		indexRequest.source(map);
		
		try {
			IndexResponse indexResponse=client.index(indexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;		
	}

	public void createIndex(String indexName) {
		RestHighLevelClient client=new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http")));
		
		CreateIndexRequest createIndexRequest=new CreateIndexRequest(indexName);
		createIndexRequest.settings(Settings.builder()
				 .put("index.number_of_shards", 2)
				 .put("index.number_of_replicas", 1));
		
		try {
			CreateIndexResponse createIndexResponse=client.indices()
					.create(createIndexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Object> getAllAdminInfo(String indexName, JSONObject json) {
		HashMap<String, Object> map=new HashMap<>();

		RestHighLevelClient client=new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http")));
		
		SearchRequest searchRequest=new SearchRequest();
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		BoolQueryBuilder query=QueryBuilders.boolQuery();
		query.must(QueryBuilders.matchQuery("network", json.get("network")))
			 .must(QueryBuilders.matchQuery("adminStatus", json.get("adminStatus")));
		searchSourceBuilder.query(query);
		searchRequest.source(searchSourceBuilder);
		
		SearchResponse searchResponse=null;
		try {
			 searchResponse=client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String adminName="";
		String userId="";
		String password="";

		SearchHits hits=searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for(SearchHit hit:hits) {
			JSONObject json1=new JSONObject(hit.getSourceAsString());
			adminName=adminName+json1.getString("adminName")+",";
			userId=userId+json1.getString("userId")+",";
			password=password+json1.getString("password")+",";
		}
		
		map.put("adminName", adminName);
		map.put("userId", userId);
		map.put("password", password);

		return map;
	}

	public HashMap<String, Object> deleteAdmin(String indexName, JSONObject json) {
		HashMap<String, Object> map=new HashMap<>();
		try {
			RestHighLevelClient client=new RestHighLevelClient(
					RestClient.builder(new HttpHost("localhost", 9200, "http")));
			
			SearchRequest searchRequest=new SearchRequest();
			SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
			BoolQueryBuilder query=QueryBuilders.boolQuery();
			query.must(QueryBuilders.matchQuery("adminName", json.get("adminName")));
			searchSourceBuilder.query(query);
			searchRequest.source(searchSourceBuilder);
			
			SearchResponse searchResponse=null;
			try {
				 searchResponse=client.search(searchRequest, RequestOptions.DEFAULT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String docId="";
			SearchHits hits=searchResponse.getHits();
			for(int i=0;i<searchResponse.getHits().getHits().length;i++) {
				docId=searchResponse.getHits().getAt(0).getId();
				DeleteRequest deleteRequest=new DeleteRequest();
				deleteRequest.index(indexName);
				deleteRequest.id(docId);
				
				DeleteResponse deleteResponse=null;
				
				deleteResponse=client.delete(deleteRequest, RequestOptions.DEFAULT);
				map.put("id", deleteResponse.getId());
				map.put("adminDetails", searchResponse.getHits().getAt(0).getSourceAsMap());
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

		
	
}
