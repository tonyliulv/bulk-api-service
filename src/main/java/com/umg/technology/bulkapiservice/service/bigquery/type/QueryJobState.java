package com.umg.technology.bulkapiservice.service.bigquery.type;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The {@code QueryJobState} class represents the job state
 * of a given {@code jobId}.
 */

@Data
@AllArgsConstructor
public class QueryJobState {
    @ApiModelProperty(notes = "Job Id of a query")
    private String jobId;

    @ApiModelProperty(notes = "Job State of a job")
    private String jobState;
}
