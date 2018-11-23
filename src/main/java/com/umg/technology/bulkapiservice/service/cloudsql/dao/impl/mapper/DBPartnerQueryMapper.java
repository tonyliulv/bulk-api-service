package com.umg.technology.bulkapiservice.service.cloudsql.dao.impl.mapper;

import com.umg.technology.bulkapiservice.controller.type.RequestQuery;
import com.umg.technology.bulkapiservice.service.cloudsql.type.PartnerQuery;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DBPartnerQueryMapper {
    PartnerQuery getPartnerQuery(final RequestQuery requestQuery);

    void insertQueryJobResult(final QueryJobResult queryJobResult);

    String getTableName(final String jobId);
}
