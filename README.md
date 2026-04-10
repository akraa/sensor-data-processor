# Sensor Data Processor

## Part 1: Manual Performance Tuning

Branch: `manual`

Goal: analyze and optimize the `calculate` function in `demo/src/main/java/com/sensordata/SensorDataProcessor.java` without AI code generation for the implementation.


### Performance Optimization Strategies (Manual Analysis Notes)

1. Hoist constant calculations (Lines 44-45)
- `Math.pow(limit[i][j], 2.0)` is recomputed inside the `k` loop.
- Since `limit[i][j]` only changes with `i` and `j`, compute it once per `(i, j)` before iterating `k`.

2. Eliminate redundant method calls (Line 47, and Line 51 impact)
- `average(data2[i][j])` is called twice per `k` iteration.
- `average(data[i][j])` is also called inside the inner loop, adding extra `O(N)` work inside an `O(N^3)` structure.
- Cache repeated averages or maintain running sums where valid.

3. Replace `Math.pow` for simple powers (Lines 44 and 50)
- Replace `Math.pow(x, 2.0)` with `x * x`.
- Replace `Math.pow(x, 3)` with `x * x * x`.
- This removes expensive generic exponentiation calls in hot paths.

4. Strength reduction and logic simplification (Line 52)
- `(i + 1) * (j + 1) > 0` is always true for zero-based indices.
- Remove this condition entirely to reduce branch work.

5. Pre-calculate inverses for division removal (Line 44)
- Replace repeated division by `d` in the innermost loop with multiplication by a precomputed `invD = 1.0 / d`.
- Multiplication is typically faster and cleaner in tight loops.

### Notes

- Keep correctness identical to baseline behavior while tuning.
- Validate each change with tests (`mvn -f demo/pom.xml clean test`).
