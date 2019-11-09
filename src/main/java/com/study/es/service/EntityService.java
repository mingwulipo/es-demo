package com.study.es.service;

import com.study.es.entity.Entity;
import com.study.es.model.PageVO;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.AvgAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.ValueCountAggregation;
import io.searchbox.indices.DeleteIndex;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 李坡
 * @date: 2018/10/1 23:39
 * @since 1.0
 */
@Slf4j
@Service
public class EntityService {
    @Autowired
    private JestClient jestClient;

    public void deleteIndex() {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(Entity.INDEX_NAME).build();

        try {
            JestResult result = jestClient.execute(deleteIndex);
            log.info(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加更新
     * @param entity
     */
    public void save(Entity entity) {
        Index index = new Index.Builder(entity).index(Entity.INDEX_NAME).type(Entity.TYPE).build();

        try {
            DocumentResult result = jestClient.execute(index);
            log.info(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量添加更新
     * @param list
     */
    public void save(List<Entity> list) {
        Bulk.Builder builder = new Bulk.Builder();
        for (Entity entity :
                list) {
            Index index = new Index.Builder(entity).index(Entity.INDEX_NAME).type(Entity.TYPE).build();
            builder.addAction(index);
        }
        Bulk bulk = builder.build();

        try {
            BulkResult result = jestClient.execute(bulk);
            log.info(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 主键查询
     * @param id
     * @return
     */
    public Entity get(String id) {
        Get get = new Get.Builder(Entity.INDEX_NAME, id).type(Entity.TYPE).build();

        try {
            DocumentResult result = jestClient.execute(get);
            log.info(result.getJsonString());
            return result.getSourceAsObject(Entity.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 条件查询
     * @param condition
     * @return
     */
    public PageVO<Entity> searchEntity(String condition) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("name", condition));
        return doSearch(builder);
    }

    /**
     * 查询全部, 仍然是默认分页10个
     * @return
     */
    public PageVO<Entity> searchAll() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        return doSearch(builder);
    }

    /**
     * 匹配查询
     * 如果只有值, 如杭州, 到所有字段匹配
     * 如果是key:value, 如name:杭州, 只到name字段匹配
     * @return
     */
    public PageVO<Entity> queryStringQuery(String queryString) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.queryStringQuery(queryString));
        return doSearch(builder);
    }


    private PageVO<Entity> doSearch(SearchSourceBuilder builder) {
        Search.Builder builder1 = new Search.Builder(builder.toString());
        Search search = builder1.addIndex(Entity.INDEX_NAME).addType(Entity.TYPE).build();
        try {
            SearchResult result = jestClient.execute(search);
            if (!result.isSucceeded()) {
                log.error("查询失败, result = {}", result.getJsonString());
                return new PageVO<>();
            }

            //默认分页查询, 只查询10个
            log.info("查询成功, result = {}", result.getJsonString());
            Long total = result.getTotal();
            log.info("total = {}", total);

            List<SearchResult.Hit<Entity, Void>> hits = result.getHits(Entity.class);
            System.out.println(hits);

            List<Entity> list = new ArrayList<>();
            for (SearchResult.Hit<Entity, Void> hit :
                    hits) {
                Entity entity = hit.source;
                list.add(entity);
            }

            return new PageVO<>(total, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 集合查询, 结果默认也是分页的, 10个
     */
    public void aggregationQuery() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        builder.query(queryBuilder);

        //分组查询
        //.field("team")报错: Fielddata is disabled on text fields by default. Set fielddata=true on [team] in order to load fielddata in memory by uninverting the inverted index.
        //映射模板将string类型的字段存进elasticsearch时，一个字符串字段有两个类型，一个text类型，分词类型；一个keyword类型，不分词类型；所以加上.keyword就可以正常聚合了
        //terms()设定分组的名称, 用这个名称拿聚合结果.默认返回10个分组bucket, .size(Integer.MAX_VALUE)返回全部分组
        //.field("age.keyword")分组不了, 数值字段聚合不用.keyword, 直接用字符名才能正确分组
        //不是分组的聚合不需要.size()改数量
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("teamAgg").field("team.keyword").size(Integer.MAX_VALUE)
                //.subAggregation(AggregationBuilders.terms("ageAgg").field("age")).size(Integer.MAX_VALUE);
                .subAggregation(AggregationBuilders.avg("ageAgg").field("age"))//team分组的平均年龄
                .subAggregation(AggregationBuilders.terms("positionAgg").field("position.keyword").size(Integer.MAX_VALUE)
                    .subAggregation(AggregationBuilders.avg("agePositionAgg").field("age"))//position分组的平均年龄
                );

        ValueCountAggregationBuilder totalCountAggregationBuilder = AggregationBuilders.count("totalCount").field("id.keyword");
        //.aggregation可以执行多次, 添加多个聚合查询
        builder.aggregation(aggregationBuilder);
        builder.aggregation(totalCountAggregationBuilder);

        Search search = new Search.Builder(builder.toString()).addIndex(Entity.INDEX_NAME).addType(Entity.TYPE).build();

        try {
            SearchResult result = jestClient.execute(search);
            log.info(result.getJsonString());

            MetricAggregation metricAggregation = result.getAggregations();
            ValueCountAggregation totalCountAggregation = metricAggregation.getAggregation("totalCount", ValueCountAggregation.class);
            System.out.println("totalCount = " + totalCountAggregation.getValueCount());

            TermsAggregation teamAgg = metricAggregation.getTermsAggregation("teamAgg");
            List<TermsAggregation.Entry> buckets = teamAgg.getBuckets();

            for (TermsAggregation.Entry entry :
                    buckets) {
                String key = entry.getKey();//不同team的值
                Long count = entry.getCount();//每个team的数量
                AvgAggregation ageAgg = entry.getAvgAggregation("ageAgg");
                Double avg = ageAgg.getAvg();
                System.out.println(key + "=" + count + "|avgAge=" + avg);

                List<TermsAggregation.Entry> positionAggBuckets = entry.getTermsAggregation("positionAgg").getBuckets();
                for (TermsAggregation.Entry positionEntry :
                        positionAggBuckets) {
                    AvgAggregation agePositionAgg = positionEntry.getAvgAggregation("agePositionAgg");
                    System.out.println(positionEntry.getKey() + "=" + positionEntry.getCount() + "|avgAge=" + agePositionAgg.getAvg());//每个队伍的不同位置和数量,平均年龄
                }

                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
