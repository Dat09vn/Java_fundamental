# Basic Spring Boot Example

This is a very simple beginner-friendly Spring Boot application.

## Project structure

- `pom.xml` — Maven project configuration
- `src/main/java/com/example/demo/BasicSpringApplication.java` — app startup class
- `src/main/java/com/example/demo/HelloController.java` — REST controller
- `src/main/java/com/example/demo/GreetingService.java` — service class
- `src/main/java/com/example/demo/Greeting.java` — response model

## Run locally

```bash
cd /home/dat123/Desktop/Java_123/Basic_Spring
mvn spring-boot:run
```

The app runs on port `19090`.

## Endpoints

- `GET /` → returns a simple JSON object
- `GET /greet/Alice` → returns a personalized JSON message
- `GET /students` → returns the full list of students
- `GET /students/1` → returns one student by id
- `POST /students` → creates a student

## Useful notes

- `@SpringBootApplication` starts the application
- `@RestController` makes the class return data directly
- `@GetMapping` maps HTTP paths to Java methods
- `@Service` keeps business logic separate
