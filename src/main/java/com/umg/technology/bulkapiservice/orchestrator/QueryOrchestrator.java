package com.umg.technology.bulkapiservice.orchestrator;

import com.umg.technology.bulkapiservice.controller.type.RequestQuery;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobGCSResult;
import com.umg.technology.bulkapiservice.service.bigquery.exception.JobNotDoneException;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobState;
import com.umg.technology.bulkapiservice.service.cloudsql.CloudSqlService;
import com.umg.technology.bulkapiservice.exception.EntityNotFoundException;
import com.umg.technology.bulkapiservice.service.cloudsql.type.PartnerQuery;
import com.umg.technology.bulkapiservice.service.bigquery.BigQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * The {@code QueryOrchestrator} class orchestrate
 * service components to implement query service logic.
 */

@Slf4j
@Component
public class QueryOrchestrator {

    private BigQueryService bigQueryService;

    private CloudSqlService cloudSqlService;

    public QueryOrchestrator(final BigQueryService bigQueryService, final CloudSqlService cloudSqlService) {
        this.bigQueryService = bigQueryService;
        this.cloudSqlService = cloudSqlService;
    }

    /**
     * Submit a BiqQuery batch job for {@code requestQuery} object.
     *
     * @param requestQuery
     *        Client's request query object.
     *
     * @return {@code QueryJobResult} object contains submiting job result.
     *
     * @throws EntityNotFoundException
     *         If the job was not submitted successfully.
     */
    public Optional<QueryJobResult> requestQuery(final RequestQuery requestQuery) throws EntityNotFoundException {

        log.info("requestQuery() start...");

        PartnerQuery partnerQuery = cloudSqlService.getPartnerQuery(requestQuery);

        QueryJobResult queryJobResult = bigQueryService.submitRequestQuery(partnerQuery);
        cloudSqlService.insertQueryJobResult(queryJobResult);

        return Optional.of(queryJobResult);
    }

    /**
     * Get job state of {@code jobId}
     *
     * @param jobId
     *      Job id
     *
     * @return Job state {@code QueryJobState}
     *
     * @throws EntityNotFoundException
     *         If {@code jobId} not exists
     */
    public QueryJobState getQueryJobState(final String jobId) throws EntityNotFoundException {
        return bigQueryService.getQueryJobState(jobId);
    }

    /**
     * Extract job result in BigQuery table to GCS.
     *
     * @param jobId
     *        Job id string
     *
     * @return {@code QueryJobGCSResult} object contain GCS result URI.
     *
     * @throws EntityNotFoundException
     *         There was no Job submitted for {@code jobId}
     * @throws JobNotDoneException
     *         Job is not finished, no result available.
     * @throws InterruptedException
     *         Extract job interrupted
     */
    public QueryJobGCSResult getQueryJobResultGCS(final String jobId) throws EntityNotFoundException, JobNotDoneException, InterruptedException {

        log.info("getQueryJobResultGCS() start...");

        String tableName = cloudSqlService.getTableName(jobId);

        return bigQueryService.getQueryJobResultGCS(jobId, tableName);

    }

}
