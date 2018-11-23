package com.umg.technology.bulkapiservice.service.cloudsql.type;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The {@code PartnerQuery} class represents the partner
 * request query.
 */

@Data
@AllArgsConstructor
public class PartnerQuery {
    /** Query Id */
    private String queryId;

    /** Query string statements */
    private String queryString;
}
