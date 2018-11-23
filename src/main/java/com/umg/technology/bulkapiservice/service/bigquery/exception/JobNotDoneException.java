package com.umg.technology.bulkapiservice.service.bigquery.exception;

/**
 * The class {@code JobNotDoneException} can be use to throw an exception
 * indicate that job with {@code jobId} is not done.
 */

public class JobNotDoneException extends Exception {
    private String jobId;

    public JobNotDoneException(final String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String getMessage() {
        return "jobId '" + jobId + "' is not done";
    }
}
