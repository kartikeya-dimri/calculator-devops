import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        try (Scanner scan = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nCalculator Demo");
                System.out.println("1. Addition");
                System.out.println("2. Subtraction");
                System.out.println("3. Multiplication");
                System.out.println("4. Division");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                String choice = scan.next();

                if (choice.equals("5")) {
                    System.out.println("Exiting calculator.");
                    break;
                }

                if (!choice.matches("[1-4]")) {
                    System.out.println("Wrong choice!");
                    continue;
                }

                System.out.print("Enter two numbers: ");

                if (!scan.hasNextFloat()) {
                    System.out.println("Invalid first number.");
                    break;
                }
                float a = scan.nextFloat();

                if (!scan.hasNextFloat()) {
                    System.out.println("Invalid second number.");
                    break;
                }
                float b = scan.nextFloat();

                try {
                    float result;

                    switch (choice) {
                        case "1":
                            result = calculator.add(a, b);
                            break;
                        case "2":
                            result = calculator.subtract(a, b);
                            break;
                        case "3":
                            result = calculator.multiply(a, b);
                            break;
                        case "4":
                            result = calculator.divide(a, b);
                            break;
                        default:
                            continue;
                    }

                    System.out.println("Result = " + result);

                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}
