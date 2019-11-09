package com.study.es.entity;

import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: 李坡
 * @date: 2018/10/2 16:16
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    public static final String INDEX = "article";
    public static final String TYPE = "article";

    @JestId
    private int id;
    private String title;
    private String content;
    private String url;
    private Date pubdate;
    private String source;
    private String author;
}
