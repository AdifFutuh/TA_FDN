package com.fdn.course.monitoring.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransformPagination {

    String[] sortArr = new String[2];

    public Map<String,Object> transformPagination(
            List ls, Page page, String column, String value
    ){
        sortArr = page.getSort().toString().split(":");
        String sortByColumn = sortArr[0];
        boolean isSorted = sortByColumn.equals("UNSORTED");
        sortByColumn = isSorted?"id": sortByColumn;
        String sort = isSorted ? "asc" : sortArr[1];

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("content", ls);
        map.put("total-data",page.getTotalElements());
        map.put("total-pages",page.getTotalPages());
        map.put("current-page",page.getNumber());
        map.put("sort", sort.trim().toLowerCase());
        map.put("sort-by", sortByColumn);
        map.put("size-per-page",page.getSize());
        map.put("column-name",column==null?"":column);
        map.put("value",value==null?"":value);
        return map;
    }
}
