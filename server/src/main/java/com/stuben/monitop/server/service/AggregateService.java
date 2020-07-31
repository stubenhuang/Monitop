package com.stuben.monitop.server.service;

import java.util.Calendar;
import java.util.Date;

import com.stuben.monitop.common.MonitorAggregate;
import com.stuben.monitop.server.dao.elasticsearch.EsDao;
import com.stuben.monitop.server.dao.po.MonitorAggregatePO;
import com.stuben.monitop.server.utils.CommonUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AggregateService {

    @Autowired
    private EsDao<MonitorAggregatePO> esDao;

    /**
     * 保存统计数据
     */
    public void save(MonitorAggregate aggregate) {
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

        esDao.saveOrUpdate(aggregatePO);
    }

    /**
     * 获取上周的统计
     * 
     */
    public MonitorAggregatePO getLastWeek(String app, int pileNo, long timestamp) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()//
                .filter(QueryBuilders.termQuery("app", app))//
                .filter(QueryBuilders.termQuery("pileNo", pileNo))//
                .filter(QueryBuilders.termQuery("dateTimestamp", DateUtils.addWeeks(parseDayDate(timestamp), -1)));

        SearchSourceBuilder sourceBuilder = SearchSourceBuilder.searchSource().query(queryBuilder).from(0).size(1);

        return esDao.query(sourceBuilder, MonitorAggregatePO.class).iterator().next();
    }

    /**
     * 获取昨天的统计
     */
    public MonitorAggregatePO getYesterday(String app, int pileNo, long timestamp) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()//
                .filter(QueryBuilders.termQuery("app", app))//
                .filter(QueryBuilders.termQuery("pileNo", pileNo))//
                .filter(QueryBuilders.termQuery("dateTimestamp", DateUtils.addDays(parseDayDate(timestamp), -1)));

        SearchSourceBuilder sourceBuilder = SearchSourceBuilder.searchSource().query(queryBuilder).from(0).size(1);

        return esDao.query(sourceBuilder, MonitorAggregatePO.class).iterator().next();
    }

    /**
     * 获取日期
     * 
     * @param timestamp
     * @return
     */
    private Date parseDayDate(long timestamp) {
        return DateUtils.truncate(new Date(timestamp), Calendar.DATE);
    }
}
