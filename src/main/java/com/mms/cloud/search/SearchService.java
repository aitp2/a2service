package com.mms.cloud.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mms.cloud.document.QueryExecutionException;
import com.mms.cloud.search.response.HitsResponse;
import com.mms.cloud.search.response.aggregations.metric.MetricResponse;
import com.mms.cloud.search.response.HitsAggsResponse;
import com.mms.cloud.search.response.query.ElasticQueryResponse;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mms.cloud.RequestMethod.GET;
import static com.mms.cloud.helper.AddIdHelper.addIdToEntity;

/**
 * Service that provides methods to execute search requests.
 */
@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private final RestClient client;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public SearchService(RestClient client, ObjectMapper jacksonObjectMapper) {
        this.client = client;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * Executes a search query using the provided template
     *
     * @param request Object containing the required parameters to execute the request
     * @param <T>     Type of resulting objects, must be mapped from json result into java entity
     * @return List of mapped objects
     */
    public <T> HitsResponse<T> queryByTemplate(SearchByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request);
            HitsResponse<T> hitsResponse = new HitsResponse<>();

            putInfoFromQueryIntoHitsResponse(request, elasticQueryResponse, hitsResponse);

            return hitsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }
    }
    
    public <T> HitsResponse<T> queryByTemplate(SearchByTemplateRequest request,String size) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request,size);
            HitsResponse<T> hitsResponse = new HitsResponse<>();

            putInfoFromQueryIntoHitsResponse(request, elasticQueryResponse, hitsResponse);

            return hitsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }
    }
    
    public <T> HitsResponse<T> queryByTemplate(SearchByTemplateRequest request,String size,String from) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request,size,from);
            HitsResponse<T> hitsResponse = new HitsResponse<>();

            putInfoFromQueryIntoHitsResponse(request, elasticQueryResponse, hitsResponse);

            return hitsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }
    }

    /**
     * Executes a search request with the provided query, but expects an aggregation part in the query. It will not
     * fail in case you do not provide an aggregation.
     *
     * @param request Object containing the required parameters to execute the request
     * @param <T>     Type of resulting objects, must be mapped from json result into java entity
     * @return Object containing the list of objects and/or the aggregations
     */
    public <T> HitsAggsResponse<T> aggsByTemplate(SearchByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request,"0");
            HitsAggsResponse<T> hitsAggsResponse = new HitsAggsResponse<>();
            putInfoFromQueryIntoHitsResponse(request,elasticQueryResponse,hitsAggsResponse);

            hitsAggsResponse.setAggregations(elasticQueryResponse.getAggregations());
            return hitsAggsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }


    }
    
    /**
     * Executes a search request with the provided query, but expects an aggregation part in the query. It will not
     * fail in case you do not provide an aggregation.
     *
     * @param request Object containing the required parameters to execute the request
     * @param <T>     Type of resulting objects, must be mapped from json result into java entity
     * @return Object containing the list of objects and/or the aggregations
     */
    public <T> HitsAggsResponse<T> aggsByTemplate(SearchByTemplateRequest request,String size) {
        Assert.notNull(request, "Need to provide a SearchByTemplateRequest object");

        try {
            ElasticQueryResponse<T> elasticQueryResponse = doExecuteQuery(request,size);
            HitsAggsResponse<T> hitsAggsResponse = new HitsAggsResponse<>();
            putInfoFromQueryIntoHitsResponse(request,elasticQueryResponse,hitsAggsResponse);

            hitsAggsResponse.setAggregations(elasticQueryResponse.getAggregations());
            return hitsAggsResponse;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a document");
        }


    }

    /**
     * Returns the number of documents in the specified index
     *
     * @param indexName The name of the index to use for counting documents
     * @return Long representing the number of documents in the provided index.
     */
    public Long countByIndex(String indexName) {
        try {
            Response response = client.performRequest(GET, indexName + "/_count");

            return jacksonObjectMapper.readValue(response.getEntity().getContent(), MetricResponse.class).getCount();

        } catch (IOException e) {
            logger.warn("Problem while executing count request.", e);
            throw new QueryExecutionException("Error when executing count request");
        }

    }

    private <T> ElasticQueryResponse<T> doExecuteQuery(SearchByTemplateRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("typed_keys", null);
        Response response = client.performRequest(
                GET,
                request.getIndexName() + "/_search?size=10000",
                params,
                new StringEntity(request.createQuery(), Charset.defaultCharset()));

        return jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());
    }
    
     /**
      * 查询可设置返回size
      * @param request
      * @param size
      * @return
      * @throws IOException
      */
    private <T> ElasticQueryResponse<T> doExecuteQuery(SearchByTemplateRequest request,String size) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("typed_keys", null);
        Response response = client.performRequest(
                GET,
                request.getIndexName() + "/_search?size="+size,
                params,
                new StringEntity(request.createQuery(), Charset.defaultCharset()));

        return jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());
    }
    
    /**
     * 查询 - 带分页
     * @param request
     * @param size
     * @return
     * @throws IOException
     */
   private <T> ElasticQueryResponse<T> doExecuteQuery(SearchByTemplateRequest request,String size,String from) throws IOException {
       Map<String, String> params = new HashMap<>();
       params.put("typed_keys", null);
       Response response = client.performRequest(
               GET,
               request.getIndexName() + "/_search?size="+size+"&from="+from,
               params,
               new StringEntity(request.createQuery(), Charset.defaultCharset()));

       return jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());
   }

    private <T> void putInfoFromQueryIntoHitsResponse(SearchByTemplateRequest request, ElasticQueryResponse<T> elasticQueryResponse, HitsResponse<T> hitsResponse) {
        List<T> hits = extractHitsByType(request, elasticQueryResponse);
        hitsResponse.setHits(hits);
        hitsResponse.setTotalHits(elasticQueryResponse.getHits().getTotal());
        hitsResponse.setTimedOut(elasticQueryResponse.getTimedOut());
        hitsResponse.setResponseTime(elasticQueryResponse.getTook());
    }

    private <T> List<T> extractHitsByType(SearchByTemplateRequest request, ElasticQueryResponse<T> elasticQueryResponse) {
        List<T> hits = new ArrayList<>();
        elasticQueryResponse.getHits().getHits().forEach(tHit -> {
            T source = tHit.getSource();
            if (request.getAddId()) {
                addIdToEntity(tHit.getId(), source);
            }
            hits.add(source);
        });
        return hits;
    }
}
