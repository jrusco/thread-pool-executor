# Java Exceptions - All You Need to Know

---

1. **Exception → Base Checked Exception**
   - This is the parent class for all checked exceptions.
   - Use when forcing caller to handle failure (e.g., file not found, network error).

2. **RuntimeException → Base Unchecked Exception**
   - This is the parent class for all unchecked exceptions.
   - Use when error is due to programming mistake (e.g., null pointer, bad cast).

3. **IOException → Checked Exception**
   - Happens during I/O failure.
   - Handle when reading/writing files or streams (e.g., reading from a file, socket input).

4. **SQLException → Checked Exception**
   - Occurs when interacting with a database.
   - Catch or throw when executing DB operations (e.g., failed DB insert/update).

5. **NullPointerException → Unchecked Exception**
   - Accessing object with null reference.
   - Avoid by null checks or using Optional (e.g., user.getName() when user is null).

6. **IllegalArgumentException → Unchecked Exception**
   - Thrown when method gets illegal or inappropriate value.
   - Use for validating input early (e.g., negative age in setAge(-1)).

7. **IllegalStateException → Unchecked Exception**
   - Called when object is in wrong state to perform operation.
   - Use when usage order is violated (e.g., using a closed resource).

8. **ArithmeticException → Unchecked Exception**
   - Happens during illegal math operation.
   - Mostly divide by zero (e.g., 10 / 0).

9. **ArrayIndexOutOfBoundsException → Unchecked Exception**
   - Accessing array index beyond its bounds.
   - Happens in bad loops or off-by-one errors (e.g., arr[10] on 5-length array).

10. **ClassCastException → Unchecked Exception**
    - Happens when type casting is invalid.
    - Common in downcasting (e.g., casting Object to String wrongly).

11. **FileNotFoundException → Checked Exception**
    - Specific subtype of IOException.
    - Happens when file path doesn’t exist (e.g., reading a missing config file).

12. **try-catch-finally → Exception Handling Block**
    - Use try to guard risky code.
    - catch handles exception.
    - finally always runs (cleanup) (e.g., closing DB/file connections).

13. **throws → Exception Declaration**
    - Use in method signature to delegate exception (e.g., public void read() throws IOException).

14. **throw → Manual Exception Throw**
    - Use to explicitly trigger exception (e.g., throw new IllegalArgumentException("bad input")).

15. **Custom Exception**

    ```java
    class MyException extends Exception {}
    ```

    Use when you want to create domain-specific error types (e.g., InvalidUserException).
