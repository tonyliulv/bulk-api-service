package com.umg.technology.bulkapiservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The {@code BigQueryConfiguration} class define configuration
 * for BigQuery client like destination dataset and table.
 */

@Data
@Component
@ConfigurationProperties("bigquery")
public class BigQueryConfiguration {
    /** dataset location */
    private String location;

    /** destination dataset name */
    private String destDataset;

    /** destination table prefix */
    private String destTablePrefix;

    /** export format like AVRO, JSON*/
    private String exportFormat;

    /** destination GCS bucket prefix*/
    private String destGcsPrefix;
}
