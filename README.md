# Bill Splitter Application

This is a full-stack application built using **Spring Boot** and **Angular** to help users split bills between friends, groups, or events. The application also sends email notifications to users upon the completion of transactions, and it uses **RabbitMQ** for asynchronous email delivery.

## Features

- Split bills between friends, groups, and events.
- Track expenses and share the total amount to be paid by each person.
- Send email notifications to users on completed transactions.
- Use RabbitMQ as a message queue to handle email delivery asynchronously.
- User authentication and registration (custom OTP-based authentication).

## Technologies Used

- **Backend**: Spring Boot, Java
- **Frontend**: Angular
- **Database**: MySQL
- **Email Service**: SMTP (for sending emails)
- **Message Queue**: RabbitMQ
- **Security**: Custom OTP authentication (without Spring Security)

## Installation

### Prerequisites

Before you start, make sure you have the following installed:

- Java 17 or later
- Node.js and npm
- MySQL
- RabbitMQ
- Maven
- Git


