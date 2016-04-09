package com.gdr.puretawny.db.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.model.Point;
import com.rethinkdb.RethinkDB;

@Service
public class DbServiceImpl implements DbService {

    private final String host;
    private final int    port;

    @Autowired
    public DbServiceImpl(@Value("${rethinkdb.host}") String host,@Value("${rethinkdb.port}") int port) {
        this.host = host;
        this.port = port;

    }

    @Override
    public void insertPoints(List<Point> points) {

    }

    @Override
    public void insertPoint(Point point) {

    }

}
