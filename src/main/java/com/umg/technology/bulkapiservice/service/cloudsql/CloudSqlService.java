package com.umg.technology.bulkapiservice.service.cloudsql;

import com.umg.technology.bulkapiservice.controller.type.RequestQuery;
import com.umg.technology.bulkapiservice.exception.EntityNotFoundException;
import com.umg.technology.bulkapiservice.service.cloudsql.type.PartnerQuery;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;
import com.umg.technology.bulkapiservice.service.cloudsql.dao.PartnerQueryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The {@code CloudSqlService} class provides cloud sql client
 * for select and update to partner query relational tables.
 */

@Service("cloudSqlService")
public class CloudSqlService {

    private static final Logger logger = LoggerFactory.getLogger(CloudSqlService.class);

    private PartnerQueryDAO partnerQueryDAO;

    public CloudSqlService(final PartnerQueryDAO partnerQueryDAO) {
        this.partnerQueryDAO = partnerQueryDAO;
    }

    public PartnerQuery getPartnerQuery(final RequestQuery requestQuery) throws EntityNotFoundException {
        logger.info("getPartnerQuery: " + requestQuery);

        PartnerQuery partnerQuery = partnerQueryDAO.getPartnerQuery(requestQuery);

        if (partnerQuery == null) {
            throw new EntityNotFoundException(PartnerQuery.class, "parterId",
                    requestQuery.getPartnerId() , "queryId", requestQuery.getQueryId());
        }

        return partnerQuery;
    }

    public void insertQueryJobResult(final QueryJobResult queryJobResult) {
        logger.info("insertQueryJobResult(): " + queryJobResult);
        partnerQueryDAO.insertQueryJobResult(queryJobResult);
    }

    public String getTableName(final String jobId) {
        logger.info("getTableName: " + jobId);
        return partnerQueryDAO.getTableName(jobId);
    }
}
