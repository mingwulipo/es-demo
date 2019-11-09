package com.study.es.entity;

import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: 李坡
 * @date: 2018/10/1 23:35
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity implements Serializable {
    public static final String INDEX_NAME = "index_entity";
    public static final String TYPE = "tstype";

    //主键必须是字符串, 不能是int
    //主键必须加注解,映射成_id, 或者主键字段名称改为_id, 但是这样不符合习惯, 还是加注解吧
    @JestId
    private String id;
    private String name;
    private Integer age;
    private Integer salary;
    private String team;
    private String position;

}
