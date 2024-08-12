```markdown
# ESOA Project

### Development Guide

1. Prerequisites:
   - Java 17
   - Maven
   - Node.js v14.17.0
   - npm 6.14.13

2. Clone the repository:

   ```
   git clone [your-repository-url]
   cd ESOA
   ```

3. Backend Development:
   - Open the project in your preferred Java IDE.
   - The main Spring Boot application is located in the `src/main/java` directory.
   - Run the Spring Boot application to start the backend server.

4. Frontend Development:
   - Navigate to the `frontend` directory:
     ```
     cd frontend
     ```
   - Install dependencies:
     ```
     npm install
     ```
   - Start the development server:
     ```
     npm start
     ```
   - The React application will be available at `http://localhost:3000`.

### Generating WAR File

To generate a WAR file that includes both the backend and frontend:

1. Ensure you're in the project root directory.

2. Run the following Maven command:
   ```
   ./mvnw clean package
   ```

3. The WAR file will be generated in the `target` directory with the name `ESOA-0.0.1-SNAPSHOT.war`.

This WAR file can be deployed to a Java application server like Tomcat.
