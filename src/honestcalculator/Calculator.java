package honestcalculator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Calculator {
    private final        Map<Integer, String> messages            = Map.of(
            0, "Enter an equation",
            1, "Do you even know what numbers are? Stay focused!",
            2, "Yes ... an interesting math operation. You've slept through all classes, haven't you?",
            3, "Yeah... division by zero. Smart move...",
            4, "Do you want to store the result? (y / n):",
            5, "Do you want to continue calculations? (y / n):",
            6, " ... lazy",
            7, " ... very lazy",
            8, " ... very, very lazy",
            9, "You are"
    );
    private static final List<String>         OPERATORS           = List.of("+", "-", "*", "/");
    private final        ConsoleIO            consoleIO;
    private static final Pattern              VALID_INT_OR_DOUBLE = Pattern.compile("^-?(\\d|[1-9]\\d*)(\\.\\d+)?$");

    public Calculator(ConsoleIO consoleIO) {
        this.consoleIO = consoleIO;
    }

    public void run() {
        boolean isFinishedCalculations = false;
        double  memory                 = 0;

        do {
            String   calc     = getEquation();
            String[] equation = calc.split(" ");
            if (equation.length != 3) {
                continue;
            }

            double x = validateNumber(equation[0], memory);
            double y = validateNumber(equation[2], memory);
            if (Double.isNaN(x) || Double.isNaN(y)) {
                consoleIO.println(messages.get(1));
                continue;
            }

            if (!OPERATORS.contains(equation[1])) {
                consoleIO.println(messages.get(2));
                continue;
            }

            check(x, y, equation[1]);

            double result = evaluateEquation(x, y, equation[1]);
            if (Double.isNaN(result)) {
                consoleIO.println(messages.get(3));
                continue;
            }
            consoleIO.println((float) result);

            String answer;
            do {
                consoleIO.println(messages.get(4));
                answer = consoleIO.getUserInput()
                                  .toLowerCase(Locale.ROOT);
                switch (answer) {
                    case "y":
                        memory = result;
                    case "n":
                    default:
                        break;
                }
            } while (!answer.equals("y") && !answer.equals("n"));

            do {
                consoleIO.println(messages.get(5));
                answer = consoleIO.getUserInput()
                                  .toLowerCase(Locale.ROOT);
                switch (answer) {
                    case "n":
                        isFinishedCalculations = true;
                    case "y":
                    default:
                }
            } while (!answer.equals("y") && !answer.equals("n"));

        } while (!isFinishedCalculations);
    }

    private String getEquation() {
        consoleIO.println(messages.get(0));
        return consoleIO.getUserInput();
    }

    /**
     * Parses and validates a string as a valid number or retrieves a stored memory value. If the input string
     * represents a valid integer or floating-point number, it is parsed into a double. If the string equals "M", the
     * given memory value is used. Otherwise, returns Double.NaN.
     *
     * @param num    the input string to be validated as a number or the character "M"
     * @param memory the stored memory value to be returned if the input string is "M"
     * @return the parsed double value, the memory value if the input is "M", or Double.NaN if invalid
     */
    private double validateNumber(String num, double memory) {
        double parsedNum = Double.NaN;
        if (VALID_INT_OR_DOUBLE.matcher(num)
                               .matches()) {
            parsedNum = Double.parseDouble(num);
        }
        if (num.equals("M")) {
            parsedNum = memory;
        }
        return parsedNum;
    }

    private double evaluateEquation(double x, double y, String operator) {
        return switch (operator) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*" -> x * y;
            case "/" -> y == 0
                        ? Double.NaN
                        : x / y;
            default -> Double.NaN;
        };
    }

    private void check(double x, double y, String operator) {
        String message = "";
        if (isOneDigit(x) && isOneDigit(y)) {
            message += messages.get(6);
        }
        if ((x == 1.0 || y == 1.0) && operator.equals("*")) {
            message += messages.get(7);
        }
        if ((x == 0.0 || y == 0.0) && !operator.equals("/")) {
            message += messages.get(8);
        }
        if (!message.isBlank()) {
            consoleIO.println(messages.get(9) + message);
        }
    }

    /**
     * Determines if a given number is a single-digit integer.
     *
     * @param num the number to be checked
     * @return true if the number is a single-digit integer, false otherwise
     */
    private boolean isOneDigit(double num) {
        if (num >= 10.0 || num <= -10.0) {
            return false;
        }
        return Double.compare(num, (int) num) == 0;
    }
}
