# Jooby Benchmarking Test

[Jooby](https://jooby.io) the modular micro web framework for Java and Kotlin.

```java
public class App extends Jooby {

  {
    get("/", ctx -> "Hello, World!");
  }

}
```


## URLs

### Plain Text Test

    http://localhost:8080/plaintext

### JSON Encoding Test

    http://localhost:8080/json

### Single Query Test

    http://localhost:8080/db

### Multiple Queries Test

    http://localhost:8080/queries

### Database updates Test

    http://localhost:8080/updates

### Fortunes Test

    http://localhost:8080/fortunes

## build

### undertow

    mvn clean package -P undertow