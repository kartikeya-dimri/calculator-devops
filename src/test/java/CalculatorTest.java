import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void testAddition() {
        assertEquals(5.0f, calculator.add(2, 3));
    }

    @Test
    void testSubtraction() {
        assertEquals(3.0f, calculator.subtract(5, 2));
    }

    @Test
    void testMultiplication() {
        assertEquals(12.0f, calculator.multiply(4, 3));
    }

    @Test
    void testDivision() {
        assertEquals(5.0f, calculator.divide(10, 2));
    }

    @Test
    void testDivisionByZero() {
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.divide(10, 0)
        );
    }
}

