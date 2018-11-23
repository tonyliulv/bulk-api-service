package com.umg.technology.bulkapiservice.service.bigquery.type;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code QueryJobGCSResult} class represents the GCS's
 * URI result of a given {@code jobId}.
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryJobGCSResult {
    @ApiModelProperty(notes = "Job Id of a query")
    private String jobId;

    @ApiModelProperty(notes = "GCS Uri object result of a query")
    private String gcsUri;
}
