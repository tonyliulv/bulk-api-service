package com.umg.technology.bulkapiservice.service.cloudsql.dao.impl;

import com.umg.technology.bulkapiservice.controller.type.RequestQuery;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;
import com.umg.technology.bulkapiservice.service.cloudsql.dao.PartnerQueryDAO;
import com.umg.technology.bulkapiservice.service.cloudsql.dao.impl.mapper.DBPartnerQueryMapper;
import com.umg.technology.bulkapiservice.service.cloudsql.type.PartnerQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * MyBatis implementation of PartnerQueryDAO
 */

@Component
public class DBPartnerQueryDAO implements PartnerQueryDAO {

    private static final Logger logger = LoggerFactory.getLogger(DBPartnerQueryDAO.class);

    private DBPartnerQueryMapper dbPartnerQueryMapper;

    public DBPartnerQueryDAO(final DBPartnerQueryMapper dbPartnerQueryMapper) {
        this.dbPartnerQueryMapper = dbPartnerQueryMapper;
    }

    public PartnerQuery getPartnerQuery(final RequestQuery requestQuery) {
        logger.info("getPartnerQuery: " + requestQuery);

        return dbPartnerQueryMapper.getPartnerQuery(requestQuery);
    }


    public void insertQueryJobResult(final QueryJobResult queryJobResult) {
        logger.info("insertQueryJobResult: " + queryJobResult);

        dbPartnerQueryMapper.insertQueryJobResult(queryJobResult);
    }

    public String getTableName(final String jobId) {
        logger.info("getTableName: " + jobId);

        return dbPartnerQueryMapper.getTableName(jobId);
    }
}
