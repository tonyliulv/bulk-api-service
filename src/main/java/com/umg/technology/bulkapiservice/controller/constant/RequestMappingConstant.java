package com.umg.technology.bulkapiservice.controller.constant;

/**
 * The {@code RequestMappingConstant} class define request mapping url constants.
 */

public final class RequestMappingConstant {

    private static final String QUERY_BASE = "/queries";
    public static final String QUERY_REQUEST = QUERY_BASE + "/requests";
    public static final String QUERY_JOB_STATE = QUERY_BASE + "/jobs/{jobId}/states";
    public static final String QUERY_JOB_RESULT_GCS = QUERY_BASE + "/jobs/{jobId}/results/gcs";
}
