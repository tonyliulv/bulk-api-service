package com.umg.technology.bulkapiservice.service.bigquery;

import com.google.cloud.RetryOption;
import com.google.cloud.bigquery.*;
import com.umg.technology.bulkapiservice.config.BigQueryConfiguration;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobGCSResult;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobState;
import com.umg.technology.bulkapiservice.service.cloudsql.type.PartnerQuery;
import com.umg.technology.bulkapiservice.service.bigquery.exception.JobNotDoneException;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;
import com.umg.technology.bulkapiservice.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.cloud.bigquery.JobInfo.CreateDisposition.CREATE_IF_NEEDED;

/**
 * The {@code BigQueryService} class provides big query client
 * for query job submission, checking job state and extract result to GCS.
 */

@Slf4j
@Service("bigQueryService")
public class BigQueryService {

    /** Configuration use by BigQuery client */
    private BigQueryConfiguration bigQueryConfiguration;

    /** BigQuery client */
    private BigQuery bigQuery;

    /**
     * Initalizes {@code bigQuery} client singleton and set its configuration.
     *
     * @param bigQueryConfiguration
     *        settings required for {@code bigQuery}
     */
    private BigQueryService(final BigQueryConfiguration bigQueryConfiguration) {
        this.bigQueryConfiguration = bigQueryConfiguration;
        bigQuery = BigQueryOptions.getDefaultInstance().getService();
    }

    /**
     * Submit a BiqQuery batch job for {@code partnerQuery} object.
     *
     * @param partnerQuery
     *        Partner's request query object.
     *
     * @return {@code QueryJobResult} object contains submiting job result.
     *
     * @throws EntityNotFoundException
     *         If the job was not submitted successfully.
     */
    public QueryJobResult submitRequestQuery(final PartnerQuery partnerQuery) throws EntityNotFoundException {
        log.info("submitRequestQuery() start...");

        String tableName = generateTableName(partnerQuery.getQueryId());

        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(partnerQuery.getQueryString())
                        // Save the results of the query to a permanent table.
                        .setDestinationTable(TableId.of(bigQueryConfiguration.getDestDataset(), tableName))
                        .setCreateDisposition(CREATE_IF_NEEDED)
                        // Run at batch priority, which won't count toward concurrent rate limit.
                        .setPriority(QueryJobConfiguration.Priority.BATCH)
                        .build();

        // Location must match that of the dataset(s) referenced in the query.
        JobId jobId = JobId.newBuilder().setRandomJob().setLocation(bigQueryConfiguration.getLocation()).build();

        // API request - starts the query.
        bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        String jobIdString = jobId.getJob();
        QueryJobState queryJobState = getQueryJobState(jobIdString);

        return new QueryJobResult(jobIdString, queryJobState.getJobState(), tableName);
    }

    /**
     * Get the state of a job.
     *
     * @param jobIdString
     *        Job's id string.
     *
     * @return {@code QueryJobState} present job's state.
     *
     *
     * @throws EntityNotFoundException
     *         If cannot retrieve the Job for {@code jobIdString}
     */
    public QueryJobState getQueryJobState(final String jobIdString) throws EntityNotFoundException {
        log.info("getQueryJobState() start...");

        // Check on the progress by getting the job's updated state. Once the state
        // is `DONE`, the results are ready.
        Job queryJob = bigQuery.getJob(
                JobId.newBuilder().setJob(jobIdString).setLocation(bigQueryConfiguration.getLocation()).build());

        if (queryJob == null)
            throw new EntityNotFoundException(Job.class, "jobId", jobIdString);

        return new QueryJobState(jobIdString, queryJob.getStatus().getState().toString());
    }

    /**
     * Extract job result in BigQuery table to GCS.
     *
     * @param jobIdString
     *        Job id string
     * @param tableName
     *        BigQuery table name
     *
     * @return {@code QueryJobGCSResult} object contain GCS result URI.
     *
     * @throws EntityNotFoundException
     *         There was no Job submitted for {@code jobIdString}
     * @throws JobNotDoneException
     *         Job is not finished, no result available.
     * @throws InterruptedException
     *         Extract job interrupted
     */
    public QueryJobGCSResult getQueryJobResultGCS(final String jobIdString, final String tableName)
                            throws EntityNotFoundException, JobNotDoneException, InterruptedException {
        log.info("getQueryJobResultGCS() start...");

        if (!getQueryJobState(jobIdString).getJobState().equals(JobStatus.State.DONE.toString()))
            throw new JobNotDoneException(jobIdString);

        Table table = bigQuery.getTable(bigQueryConfiguration.getDestDataset(), tableName);
        String gcsUri = generateGCSUri(tableName);
        Job job = table.extract(bigQueryConfiguration.getExportFormat(), gcsUri);
        // Wait for the job to complete
        Job completedJob =
                job.waitFor(
                        RetryOption.initialRetryDelay(Duration.ofSeconds(1)),
                        RetryOption.totalTimeout(Duration.ofMinutes(3)));

        if (completedJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (completedJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(completedJob.getStatus().getError().toString());
        }

        return new QueryJobGCSResult(jobIdString, gcsUri);
    }

    /**
     * Generate destination table name based on destination table prefix, {@code jobIdString}
     * and current timestamp.
     *
     * @param jobIdString
     *        Job id string
     *
     * @return Destination table name
     */
    private String generateTableName(final String jobIdString) {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

        return bigQueryConfiguration.getDestTablePrefix() + "_" + jobIdString + "_" + timeStamp;
    }

    /**
     * Generate destination GCS URI based on destination GCS prefix, {@code tableName}
     * and export format.
     *
     * @param tableName
     *        Destination table name
     *
     * @return Destination GCS URI
     */
    private String generateGCSUri(final String tableName) {
        return bigQueryConfiguration.getDestGcsPrefix() + tableName + "." + bigQueryConfiguration.getExportFormat().toLowerCase();
    }

}
