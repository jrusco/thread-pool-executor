# One Liner Explanation to Software Engineering Principles

---

## Code Design Principles

1. **DRY (Don't Repeat Yourself)** - Avoid duplicating logic, abstract reusable code.
2. **KISS (Keep It Simple, Stupid)** - Prefer simple solutions over clever or complex ones.
3. **YAGNI (You Aren't Gonna Need It)** - Don’t build features until they’re actually needed.
4. **SOLID** - A set of 5 OOP principles for robust and extensible code (see below).
5. **Separation of Concerns** - Keep different functionalities isolated in different modules/layers.
6. **Single Responsibility Principle (SRP)** - A class should have one and only one reason to change.
7. **Open/Closed Principle (OCP)** - Code should be open for extension, but closed for modification.
8. **Liskov Substitution Principle (LSP)** - Subtypes must be replaceable for base types without breaking behavior.
9. **Interface Segregation Principle (ISP)** - Prefer many small, specific interfaces over one large, generic one.
10. **Dependency Inversion Principle (DIP)** - Depend on abstractions, not on concrete implementations.

## Architecture & System Design Principles

11. **High Cohesion** - Group related functionality together to make code easier to maintain.
12. **Low Coupling** - Minimize dependencies between components to reduce the impact of changes.
13. **Modularity** - Break systems into independent modules that can be developed and tested in isolation.
14. **Composition over Inheritance** - Favor building behavior via composition rather than deep inheritance trees.
15. **Don't Call Us, We'll Call You (Hollywood Principle)** - Let frameworks control flow, not you.
16. **Design for Failure** - Assume things will break, build systems to tolerate and recover from failure.
17. **Least Privilege** - Give components/users only the permissions they absolutely need.
18. **Fail Fast** - Detect and stop errors early instead of letting them propagate silently.
19. **Graceful Degradation** - Design systems to continue operating in a reduced mode if something breaks.
20. **Loose Coupling, Tight Cohesion** - Components should be internally focused, externally agnostic.

## Thinking & Workflow Principles

21. **You Build It, You Run It** - Developers should also be responsible for deployment and monitoring.
22. **Principle of Least Surprise** - Your code should behave in ways users and developers intuitively expect.
23. **Documentation is a Feature** - Good code isn’t complete without good documentation.
24. **Readability Over Cleverness** - Prioritize clarity for the next person reading your code (which might be you).
25. **Automate Repetitive Tasks** - Anything you do twice should be scripted.
26. **Start with the End in Mind** - Design with use cases and edge cases before jumping into code.
27. **Code for Testability** - Write code in a way that makes it easy to unit test.
28. **Optimize Last** - First make it work, then make it clean, then make it fast.
29. **Think in Trade-offs** - Every decision has costs, choose based on context, not dogma.
30. **Prefer Explicit Over Implicit** - Clear, intentional code is better than magic behind
