package com.umg.technology.bulkapiservice.service.cloudsql.dao;

import com.umg.technology.bulkapiservice.controller.type.RequestQuery;
import com.umg.technology.bulkapiservice.service.cloudsql.type.PartnerQuery;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;

/**
 *  DAO interfaces for CRUD functionalities
 */

public interface PartnerQueryDAO {
    PartnerQuery getPartnerQuery(final RequestQuery requestQuery);

    void insertQueryJobResult(final QueryJobResult queryJobResult);

    String getTableName(final String jobId);
}

