package com.study.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 李坡
 * @date: 2018/10/2 20:16
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> {
    private long total;
    private List<T> list;
}
