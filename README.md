
# ChâTop Rentals API

## Description

This REST API is built with Spring Boot and uses a MySQL database. It is secured with JWT tokens and documented via SpringDoc (OpenAPI).

## Features

* User Management
* Rentals Management
* Resource protection with JWT authentication
* Dynamic documentation via Swagger UI
* Integration with a MySQL database

## Prerequisites

* **Java 17** or higher
* **Maven 3.8+** for dependency management
* **MySQL 8.0+** for the database

## Installation

1. **Setting Up the Database**

    1. Install MySQL following the official guide: [Install MySQL](https://dev.mysql.com/downloads/installer/)

    2. Create the "rentals_db" database for the API:
    ```
    CREATE DATABASE `rentals_db`;
    ```

    3. Add a user with the necessary privileges: 

    ```
    CREATE USER 'api_user'@'localhost' IDENTIFIED BY 'password';
    GRANT ALL PRIVILEGES ON rentals_db.* TO 'api_user'@'localhost';
    FLUSH PRIVILEGES;
    ```



2. **Generating JWT Keys**

    1. Install OpenSSL if not already installed.

        **For Windows**
        
        You can download the latest OpenSSL binary from [https://kb.firedaemon.com/support/solutions/articles/4000121705#Download-OpenSSL](https://kb.firedaemon.com/support/solutions/articles/4000121705#Download-OpenSSL)

        Ensure to select the option to add OpenSSL to the system's PATH.

        **For macOS**

        Install OpenSSL using HomeBrew:

        ```
        brew install openssl
        ```

        **For Linux**

        Use your system's package manager to install OpenSSL:

        ```
        # Ubuntu/Debian

        sudo apt update
        sudo apt install openssl
        ```

        **Verify the installation**

        ```
        openssl version
        ```

    2. Generate a pair of private and public keys named ```app.key``` and ```app.pub```:

    ```
    # Generate private key
    openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:2048

    # Extract public key
    openssl rsa -pubout -in app.key -out app.pub

    ```

3. **Running the Application**

    1. Clone the project:

    ```
    git clone https://github.com/arnaud-romil/rentals-api.git
    cd rentals-api
    ```

    2. Update the ```application.yaml``` file with your database settings. 

    ```
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/rentals_db
        username: api_user
        password: password
    ```

    3. Move the ```app.key``` and ```app.pub``` files to the ```src/main/resources``` directory. The ```.gitignore``` file already contains the following lines to ensure the keys are not published:
    
    ```
    src/main/resources/app.key
    src/main/resources/app.pub
    ```

    4. Build and start the project using Maven:
    
    ```
    mvn spring-boot:run
    ```

    5. The API will be available at: ```http://localhost:3001/api/```.

## Documentation

The API documentation is generated using SpringDoc and is available via Swagger UI:

* Swagger UI URL: [http://localhost:3001/api/swagger-ui.html](http://localhost:3001/api/swagger-ui.html)
* OpenAPI JSON URL: [http://localhost:3001/api/v3/api-docs](http://localhost:3001/api/v3/api-docs)


## Best Practices

* **Secure JWT Keys**: Ensure private keys are never exposed publicly.

## Contributing

Contributions are welcome! Please submit an issue or pull request for any improvements or fixes.