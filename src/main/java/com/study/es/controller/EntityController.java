package com.study.es.controller;

import com.study.es.entity.Entity;
import com.study.es.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author: 李坡
 * @date: 2018/10/2 0:42
 * @since 1.0
 */
@RestController
@RequestMapping("/city")
public class EntityController {
    private List<String> cityList = Arrays.asList("北京", "上海", "杭州", "东京", "纽约", "南京", "深圳", "香港");
    private Random random = new Random();

    @Autowired
    private EntityService cityService;

    @PostMapping("/deleteIndex")
    public Object deleteIndex() {
        cityService.deleteIndex();
        return "ok";
    }

    @PostMapping("/add")
    public Object add(Entity entity) {
        cityService.save(entity);
        return "ok";
    }

    @PostMapping("/batchAdd")
    public Object batchAdd() {
        List<Entity> list = new ArrayList<>();

        list.add(new Entity( "1", "james", 33, 3000, "cav", "sf"));
        list.add(new Entity("2", "irving", 25, 2000, "cav", "pg"));
        list.add(new Entity("3", "curry", 29, 1000, "war", "pg"));
        list.add(new Entity( "4", "thompson", 26, 2000, "war", "sg"));
        list.add(new Entity( "5", "green", 36, 2000, "war", "pf"));
        list.add(new Entity( "6", "garnett", 40, 1000, "tim", "pf"));
        list.add(new Entity( "7", "towns", 22, 500, "tim", "c"));
        list.add(new Entity( "8", "lavin", 21, 300, "mancheng", "sg"));
        list.add(new Entity( "9", "wigins", 20, 500, "tim", "sf"));
        list.add(new Entity( "10", "tom", 34, 500, "mancheng", "sf"));
        list.add(new Entity( "11", "jerry", 28, 600, "tim", "c"));
        list.add(new Entity( "12", "lihong", 32, 500, "lvcheng", "sf"));
        list.add(new Entity( "13", "lixue", 20, 500, "tim", "sf"));
        list.add(new Entity( "14", "dabing", 30, 500, "lvcheng", "sg"));
        list.add(new Entity( "15", "ergou", 23, 500, "tim", "sg"));
        list.add(new Entity( "16", "wangwu", 29, 500, "lvcheng", "sf"));

        cityService.save(list);
        return "ok";
    }

    @PostMapping("/get")
    public Object get(String id) {
        return cityService.get(id);
    }

    @PostMapping("/searchAll")
    public Object searchAll() {
        return cityService.searchAll();
    }

    @PostMapping("/searchEntity")
    public Object searchEntity(String name) {
        return cityService.searchEntity(name);
    }

    @PostMapping("/queryStringQuery")
    public Object queryStringQuery(String queryString) {
        return cityService.queryStringQuery(queryString);
    }

    @PostMapping("/aggregationQuery")
    public Object aggregationQuery() {
        cityService.aggregationQuery();
        return "ok";
    }

}
