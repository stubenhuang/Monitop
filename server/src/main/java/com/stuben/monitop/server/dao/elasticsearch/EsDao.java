package com.stuben.monitop.server.dao.elasticsearch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.stuben.monitop.common.MonitorAggregate;
import com.stuben.monitop.server.dao.po.MonitorAggregatePO;
import com.stuben.monitop.server.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

@Repository
public class EsDao<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 保存
     *
     * @param po
     */
    public void saveOrUpdate(T po) {
        if (po == null) {
            logger.warn("EsDataList is null or empty.");
            return;
        }

        IndexRequest indexRequest = buildIndexRequest(po);
        if (null == indexRequest) {
            logger.warn("null indexRequest , po:{}", JSON.toJSONString(po));
            return;
        }

        try {
            restHighLevelClient.index(indexRequest);
        } catch (IOException e) {
            logger.error("saveOrUpdate", e);
        }
    }

    /**
     * 查询
     *
     * @param sourceBuilder
     * @param clz
     * @return
     */
    public List<T> query(SearchSourceBuilder sourceBuilder, Class<T> clz) {
        Pair<String, String> topicAndType = getTopicAndType(clz);
        if (null == topicAndType) {
            logger.warn("query , null topicAndType , clz:{}", clz);
            return Collections.emptyList();
        }

        Field idField = getIdField(clz);
        if (null == idField) {
            logger.warn("query , null id field , clz:{}", clz);
            return Collections.emptyList();
        }

        try {
            SearchRequest searchRequest = new SearchRequest(topicAndType.getLeft()).types(topicAndType.getRight()).source(sourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

            SearchHit[] hits = searchResponse.getHits().getHits();
            List<T> result = Lists.newArrayListWithCapacity(hits.length);
            for (SearchHit hit : hits) {
                T obj = JSON.parseObject(hit.getSourceAsString(), clz, Feature.AllowISO8601DateFormat);
                Object idObj = FieldUtils.readField(idField, obj, true);
                if (null == idObj) {
                    FieldUtils.writeField(idField, obj, hit.getId(), true);
                }
                result.add(obj);
            }

            return result;

        } catch (Exception e) {
            logger.warn("query , e:{}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 构建索引的index
     * 
     * @param po
     * @return
     */
    private IndexRequest buildIndexRequest(T po) {

        Class<?> clz = po.getClass();

        Pair<String, String> topicAndType = getTopicAndType(clz);
        if (null == topicAndType) {
            logger.warn("buildIndexRequest , null topicAndType , class:{}", clz);
            return null;
        }

        try {
            Object idObj = FieldUtils.readField(getIdField(clz), po, true);
            return new IndexRequest(topicAndType.getLeft(), topicAndType.getRight(), null == idObj ? null : idObj.toString())
                    .source(JSON.toJSONStringWithDateFormat(po, "yyyy-MM-dd'T'HH:mm:ss+08:00"), XContentType.JSON);
        } catch (Exception e) {
            logger.warn("buildIndexRequest , id is illeage , class:{}", clz, e);
            return null;
        }

    }

    /**
     * 获取id的域
     * 
     * @param clz
     * @return
     */
    public Field getIdField(Class<?> clz) {
        List<Field> listWithAnnotation = FieldUtils.getFieldsListWithAnnotation(clz, Id.class);
        if (listWithAnnotation.size() != 1) {
            logger.warn("getIdField , id is illeage , class:{}", clz);
            return null;
        }
        return listWithAnnotation.get(0);
    }

    /**
     * 获取topic和type
     * 
     * @param clz
     * @return
     */
    private Pair<String/* topic */, String/* type */> getTopicAndType(Class<?> clz) {
        EsDeclare esDeclare = clz.getAnnotation(EsDeclare.class);
        if (null == esDeclare || StringUtils.isEmpty(esDeclare.index())) {
            logger.warn("getTopicAndType , esDeclare is illegal , class:{}", clz);
            return null;
        }
        return Pair.of(esDeclare.index(), clz.getSimpleName());
    }

    public static void main(String[] args) {
        String jsonStr = "{\"app\":\"test4\",\"average\":24,\"count\":1399,\"dateTimestamp\":1566403212345,\"max\":79,\"min\":1,\"pileNo\":1,\"sum\":34344}";
        MonitorAggregate aggregate = JSON.parseObject(jsonStr, MonitorAggregate.class);

        MonitorAggregatePO aggregatePO = new MonitorAggregatePO();
        aggregatePO.setId(CommonUtils.getMongoId(aggregate.getApp(), aggregate.getPileNo(), aggregate.getDateTimestamp()));
        aggregatePO.setApp(aggregate.getApp());
        aggregatePO.setPileNo(aggregate.getPileNo());
        aggregatePO.setAverage(aggregate.getAverage());
        aggregatePO.setCount(aggregate.getCount());
        aggregatePO.setMax(aggregate.getMax());
        aggregatePO.setMin(aggregate.getMin());
        aggregatePO.setSum(aggregate.getSum());
        aggregatePO.setDayTime(new Date(aggregate.getDateTimestamp()));

        System.out.println(JSON.toJSONString(aggregatePO, SerializerFeature.UseISO8601DateFormat));
        System.out.println(JSON.toJSONStringWithDateFormat(aggregatePO, "yyyy-MM-dd'T'HH:mm:ss+08:00"));
    }

}
