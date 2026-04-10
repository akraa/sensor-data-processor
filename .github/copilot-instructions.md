# Project Guidelines

## Code Style
- Use Java 17-compatible code and keep changes small and focused.
- Follow existing package structure under `com.sensordata`.
- Keep production code in `demo/src/main/java` and tests in `demo/src/test/java`.

## Architecture
- Core logic is in `demo/src/main/java/com/sensordata/SensorDataProcessor.java`.
- `SensorDataProcessor.calculate(double d)` processes 3D sensor data and writes output to `RacingStatsData.txt` in the current working directory.
- `demo/src/main/java/com/sensordata/SensorDataProcessorApp.java` is a demo entrypoint for local runs.

## Build and Test
- Run commands from `demo/` unless a task says otherwise.
- Build: `mvn clean compile`
- Test: `mvn clean test`
- Convenience script: `./run-tests.sh`
- Coverage report is generated at `demo/target/site/jacoco/index.html` after tests.

## Conventions
- Test framework is JUnit 5 (Jupiter) with Maven Surefire.
- Prefer branch-focused tests for `calculate(...)` behavior and edge cases.
- Tests that execute `calculate(...)` should account for file I/O side effects (`RacingStatsData.txt`) and clean up state as needed.

## References
- Coverage test intent and branch mapping: `TEST_COVERAGE_ANALYSIS.md`.