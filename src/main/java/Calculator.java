public class Calculator {

    public float add(float a, float b) {
        return a + b;
    }

    public float subtract(float a, float b) {
        return a - b;
    }

    public float multiply(float a, float b) {
        return a * b;
    }

    public float divide(float a, float b) {
        if (b == 0) {
            throw new IllegalArgumentException(
                "Division by zero is not allowed"
            );
        }
        return a / b;
    }
}

