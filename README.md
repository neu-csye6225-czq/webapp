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

## Assignment 03

### Bootstrapping Database

- The application is expected to automatically bootstrap the database at startup.
- Bootstrapping creates the schema, tables, indexes, sequences, etc., or updates them if their definition has changed.
- The database cannot be set up manually by running SQL scripts.
- It is highly recommended that you use ORM frameworks such as Hibernate (for java), SQLAlchemy (for python), and
  Sequelize (for Node.js).

### Users & User Accounts

1. The web application will load account information from a CSV file from well known location `/opt/user.csv`.
2. The application should load the file at startup and create users based on the information provided in the CSV file.
    - a. The application should create new user account if one does not exist.
    - b. If the account already exists, no action is required i.e. no updates.
    - c. Deletion is not supported.

3. Example CSV file can be downloaded from [here](https://fall2023.csye6225.cloud/assignments/a3/users.csv).
4. The user's password must be hashed using [BCrypt](https://en.wikipedia.org/wiki/Bcrypt) before it is stored in the database.
   Users should not be able to set values for `account_created` and `account_updated`. Any value provided for these fields
   must be ignored.

### Authentication Requirements
1. The user must provide a basic authentication token when making an API call to the authenticated endpoint.
2. The web application must only support Token-Based authentication and not Session Authentication.

### Implement Continuous Integration (CI) with GitHub Actions
1. Add a GitHub Actions workflow to run the application integration tests for each pull request raised. A pull request can only be merged if the workflow executes successfully.
2. Add Status Checks GitHub branch protection to prevent users from merging a pull request when the GitHub Actions workflow run fails.
3. The CI check should run the integration tests you will implement as part of this assignment.

### Implement Integration Tests
Implement integration (and not unit) tests for the /healtz endpoint. You only need to test for success criteria and not for the failure. This will require your GitHub action to install and setup an actual MySQL and PostgreSQL instance and provide configuration to the application to connect to it.

ADD something