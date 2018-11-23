package com.umg.technology.bulkapiservice.service.bigquery.type;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The {@code QueryJobResult} class represents the job state and
 * BigQuery result table name of a given {@code jobId}.
 */

@Data
@AllArgsConstructor
public class QueryJobResult {
    @ApiModelProperty(notes = "Job Id of a query")
    private String jobId;

    @ApiModelProperty(notes = "Job State of a job")
    private String jobState;

    @ApiModelProperty(notes = "BigQuery table name store result of a query")
    private String tableName;
}
