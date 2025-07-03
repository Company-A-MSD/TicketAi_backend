# TicketAi Backend

This project is a backend application for TicketAi.

## Description

This is a Spring Boot application that provides backend services for TicketAi.  It includes features for user management, ticket management, and employee management.

## Technologies Used

* Java 21
* Spring Boot 3.4.5
* MongoDB
* Spring Security
* JWT
* Lombok
* Spring AI (Hugging Face and Azure OpenAI)


## Dependencies

The following dependencies are used in this project:

* `spring-boot-starter-actuator`
* `spring-boot-starter-data-mongodb`
* `spring-boot-starter-security`
* `spring-boot-starter-web`
* `spring-boot-devtools`
* `spring-boot-configuration-processor`
* `lombok`
* `spring-boot-starter-test`
* `spring-security-test`
* `java-dotenv`
* `jjwt-api`
* `jjwt-impl`
* `jjwt-jackson`
* `spring-boot-starter-mail`
* `spring-ai-starter-model-huggingface`
* `spring-ai-starter-model-azure-openai`


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Java 21
* Maven
* MongoDB

### Installation

1. Clone the repository: `git clone [repository_url]`
2. Navigate to the project directory: `cd TicketAi_backend`
3. Install dependencies: `mvn install`
4. Run the application: `mvn spring-boot:run`

## Adding .env Secrets

Create a `.env` file in the root directory of the project.  This file should contain your application secrets in the following format:

```
MONGODB_URI=your-mongodb-database-uri
MONGODB_DATABASE=your-mongodb-database
HUGGINGFACE_API_KEY=your-huggingface-api-key
HUGGINGFACE_ENDPOINT_URL=your-huggingface-endpoint
```

The `java-dotenv` library will automatically load these secrets at runtime.

