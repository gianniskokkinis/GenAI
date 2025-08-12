# Automated Document Management System

## Introduction
This project concerns the development of an automated document management system designed to:
- Optimizes workflow
- Reduces human error
- Increases the efficiency of business processes

The system enables automated data capture, processing, and storage from multiple sources (forms, emails, invoices), eliminating manual processing.

## Installation Instructions

### Requirements
- Install MySQL Server


### Setup Database
1. **User and database creation**:
   ```sql
   CREATE USER 'genai'@'localhost' IDENTIFIED BY 'genai';
   GRANT ALL PRIVILEGES ON *.* TO 'genai'@'localhost' WITH GRANT OPTION;
   FLUSH PRIVILEGES;
   CREATE DATABASE genaidb;

2. **Initialization of structure**:
   ```sql
   SOURCE <path_to_resources>/genai.sql;


### System Execution
Inserting into IDE (Eclipse)
1. **clone repository:**:
   ```bash
   git clone https://github.com/gianniskokkinis/GenAI.git

2. **Insert as Existing Maven Project**
3. **Run GenAiApplication.java**

### ⚠️ Attention
The credentials for the Google API in **GoogleSheetsController** must be entered manually for security reasons.