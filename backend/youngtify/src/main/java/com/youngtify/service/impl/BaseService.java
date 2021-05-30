package com.youngtify.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class BaseService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public <T> T firstOrDefault(Class<T> returnType, String storeName, Object ...args){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(storeName)
                .returningResultSet("resultSet", BeanPropertyRowMapper.newInstance(returnType));
        List<T> lst = (List<T>) (simpleJdbcCall
                .execute(args))
                .get("resultSet");
        if (lst.isEmpty())
            return null;
        return lst.get(0);
    }

    public <T> List<T> getAll(Class<T> returnType, String storeName, Object ...args){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(storeName)
                .returningResultSet("resultSet", BeanPropertyRowMapper.newInstance(returnType));
        return (List<T>) (simpleJdbcCall
                .execute(args))
                .get("resultSet");
    }

    public int executeNoneQuery(String storeName, Object ...args){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(storeName)
                .returningResultSet("resultSet", (resultSet, i) -> resultSet.getInt("temp"));
        List<Integer> rs =  (List<Integer>)(simpleJdbcCall
                .execute(args))
                .get("resultSet");
        return rs.get(0);
    }
}
