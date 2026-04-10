# Project Summary

Earlier code changes focused on making `calculate()` faster without introducing new classes or external files. The main updates were:

- Cached `data`, `limit`, and dimension values to avoid repeated lookups.
- Replaced repeated division with multiplication by a precomputed reciprocal.
- Removed repeated `Math.pow` calls in favor of direct multiplication.
- Switched to try-with-resources for cleaner file handling.
- Reduced memory pressure by removing the full 3D temporary array.
- Buffered each row before writing output to reduce file I/O overhead.

These changes kept the existing class structure intact while improving constant factors and reducing unnecessary allocations.

---

# Comparison & Analysis Report

## Performance & Strategy
The AI did suggest a few valid optimization strategies that were not always obvious in a first manual pass: caching array dimensions, replacing repeated division with multiplication by a reciprocal, removing repeated `Math.pow` calls, and using try-with-resources for safer file handling. Those are real constant-factor improvements.

It also produced a few changes that were either too aggressive or not fully aligned with the original intent. The main risk was changing loop semantics while trying to optimize them. In particular, optimizing the average checks inside the innermost loop made the logic harder to reason about, and some of the early rewrites increased complexity without a matching gain in correctness or clarity.

## Code Quality
The traditional workflow produced cleaner and more maintainable results because it stayed focused on invariants first: preserve behavior, then optimize only the redundant work. The AI workflow was faster at generating code, but it often mixed performance ideas with structural changes that were harder to verify.

The manual approach was better at preserving edge cases and behavioral constraints, such as zero-divisor handling, output compatibility, and branch behavior. The AI occasionally optimized in ways that were mathematically awkward or reduced readability, even when the code compiled.

## Efficiency & Cognitive Load
The AI was faster at proposing edits, but not necessarily faster end-to-end. A significant amount of time was spent validating, challenging, and correcting the generated code. That verification cost reduced much of the speed advantage.

From a complexity standpoint, the core loop remains $O(rows \cdot cols \cdot depth)$ because each sensor value still has to be examined. The AI improved constant factors, but it did not change the fundamental asymptotic cost.

## Prompt Engineering Effectiveness
The most effective prompts were narrow and constraint-heavy, such as asking for a strict performance rewrite, limiting changes to the existing class, and asking directly whether the algorithmic complexity could be improved. Those prompts reduced scope and forced the AI to focus on concrete optimizations.

It still took multiple iterations to reach an acceptable result. The best challenges were the ones that forced the AI to distinguish between true optimization and code that merely looked faster. That pressure improved the outcome more than open-ended requests did.

## Actionable Takeaways
The right balance is to use traditional performance engineering for algorithm choice, invariants, and correctness, then use AI for quick constant-factor improvements and mechanical refactors. In future work, I would first determine the true complexity manually, then use AI to draft targeted optimizations, and finally verify that the result preserves behavior and readability.