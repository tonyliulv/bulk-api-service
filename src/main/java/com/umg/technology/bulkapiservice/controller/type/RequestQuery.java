package com.umg.technology.bulkapiservice.controller.type;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * A class present client input parameters for a query request.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestQuery {

    @ApiModelProperty(notes = "Partner Id of a request")
    @NotBlank(message = "Partner Id is required")
    private String partnerId;

    @ApiModelProperty(notes = "Query Id of a request")
    @NotBlank(message = "Query Id is required")
    private String queryId;
}
