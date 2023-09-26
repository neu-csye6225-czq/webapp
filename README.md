# CSYE6225-webapp

## Assignment 02 

### Health Check RESTful API

For this assignment, we are going to implement an endpoint `/healthz` that will do the following when called:

1. Check if the application has connectivity to the database.
2. Return `HTTP 200 OK` if the connection is `successful`.
3. Return `HTTP 503 Service Unavailable` if the connection is `unsuccessful`.
4. The API response should not be cached. Make sure to add `cache-control: 'no-cache'` header to the response.
5. The API request should not allow for any payload.
6. The API response should not include any payload.
7. Only HTTP `GET` method is supported for the `/healthz` endpoint.


