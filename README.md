# inMemoryHandlerReproducer

This project reproduces problem with catching and verifying logs in tests. We use `InMemoryHandler` found in `junit5-internal` dependency.

To reproduce the issue run the two following scenarios.

1. Works as expected
```
./mvnw clean quarkus:test
```

2. Does not work as expected
```
./mvnw clean quarkus:dev
```
> **Note**: You have to run tests, i.e. press 'r' when it becomes available

You will find the second scenario to fail. It fails on second assertion, trying to find `"debug message"`.