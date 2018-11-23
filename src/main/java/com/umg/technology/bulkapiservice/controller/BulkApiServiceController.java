package com.umg.technology.bulkapiservice.controller;

import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobGCSResult;
import com.umg.technology.bulkapiservice.controller.type.RequestQuery;
import com.umg.technology.bulkapiservice.orchestrator.QueryOrchestrator;
import com.umg.technology.bulkapiservice.service.bigquery.exception.JobNotDoneException;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobResult;
import com.umg.technology.bulkapiservice.service.bigquery.type.QueryJobState;
import com.umg.technology.bulkapiservice.exception.EntityNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umg.technology.bulkapiservice.controller.constant.RequestMappingConstant.*;

@Slf4j
@RestController
@RequestMapping("/bulkApi")
@Api(description = "Operations pertaining to BigQuery requests")
public class BulkApiServiceController {

    // TODO check log level and respones type

    private QueryOrchestrator queryOrchestrator;

    public BulkApiServiceController(final QueryOrchestrator queryOrchestrator) {
        this.queryOrchestrator = queryOrchestrator;
    }

    @ApiOperation(value = "Request a query job", response = QueryJobResult.class, produces = "application/json")
    @RequestMapping(value = QUERY_REQUEST, method = RequestMethod.POST)
    public ResponseEntity<QueryJobResult> requestQuery(@Valid @RequestBody final RequestQuery requestQuery)
            throws EntityNotFoundException {

        log.info("requestQuery() start...");

        QueryJobResult queryJobResult = queryOrchestrator.requestQuery(requestQuery).orElse(null);

        return new ResponseEntity<>(queryJobResult, HttpStatus.ACCEPTED);
    }

    //@ApiOperation(value = "Get state of a query job", response = QueryJobState.class, produces = "application/json")
    @RequestMapping(value = QUERY_JOB_STATE, method = RequestMethod.GET)
    public ResponseEntity<QueryJobState>  getQueryJobState(@PathVariable final String jobId) throws EntityNotFoundException {

        log.info("getQueryJobState() start...");

        return new ResponseEntity<>(queryOrchestrator.getQueryJobState(jobId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get query result in GCS", response = QueryJobGCSResult.class, produces = "application/json")
    @RequestMapping(value = QUERY_JOB_RESULT_GCS, method = RequestMethod.GET)
    public ResponseEntity<QueryJobGCSResult> getQueryJobResultGCS(@PathVariable final String jobId) throws EntityNotFoundException, JobNotDoneException, InterruptedException {

        log.info("getQueryJobResultGCS() start...");

        return new ResponseEntity<>(queryOrchestrator.getQueryJobResultGCS(jobId), HttpStatus.OK);
    }

}
