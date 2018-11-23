# bulk-api-service

A simple demo to show how to integrate Google AppEngine, Cloud Sql, BigQuery and GCS in a RESTful api using Spring Boot.

## Compile/Run
```
# run locally
export GOOGLE_APPLICATION_CREDENTIALS='service-account-file.json'

mvn appengine:run

# run on gcp
mvn appengine:deploy

# swagger ui
http://localhost:8080/swagger-ui.html#/
```
