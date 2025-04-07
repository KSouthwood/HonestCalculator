package honestcalculator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Calculator {
    private final        Map<Integer, String> messages    = Map.of(
            0, "Enter an equation",
            1, "Do you even know what numbers are? Stay focused!",
            2, "Yes ... an interesting math operation. You've slept through all classes, haven't you?",
            3, "Yeah... division by zero. Smart move...",
            4, "Do you want to store the result? (y / n):",
            5, "Do you want to continue calculations? (y / n):"
    );
    private static final List<String>         OPERATORS   = List.of("+", "-", "*", "/");
    private final        ConsoleIO            consoleIO;
    private static final Pattern              INT_REGEX   = Pattern.compile("^-?(\\d|[1-9]\\d*)$");
    private static final Pattern              FLOAT_REGEX = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final Pattern              VALID_INT_OR_FLOAT = Pattern.compile("^-?(\\d|[1-9]\\d*)(\\.\\d+)?$");

    public Calculator(ConsoleIO consoleIO) {
        this.consoleIO = consoleIO;
    }

    public void run() {
        boolean isFinishedCalculations = false;
        float memory = 0;

        do {
            String   calc     = getEquation();
            String[] equation = calc.split(" ");
            if (equation.length != 3) {
                continue;
            }

            float x = validateNumber(equation[0], memory);
            float y = validateNumber(equation[2], memory);
            if (Float.isNaN(x) || Float.isNaN(y)) {
                consoleIO.println(messages.get(1));
                continue;
            }

            if (!OPERATORS.contains(equation[1])) {
                consoleIO.println(messages.get(2));
                continue;
            }

            float result = evaluateEquation(x, y, equation[1]);
            if (Float.isNaN(result)) {
                consoleIO.println(messages.get(3));
                continue;
            }
            consoleIO.println(result);

            String answer;
            do {
                consoleIO.println(messages.get(4));
                answer = consoleIO.getUserInput().toLowerCase(Locale.ROOT);
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
                answer = consoleIO.getUserInput().toLowerCase(Locale.ROOT);
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
     * Parses and validates a string as a valid number or retrieves a stored memory value.
     * If the input string represents a valid integer or floating-point number, it is parsed
     * into a float. If the string equals "M", the given memory value is used. Otherwise,
     * returns Float.NaN.
     *
     * @param num the input string to be validated as a number or the character "M"
     * @param memory the stored memory value to be returned if the input string is "M"
     * @return the parsed float value, the memory value if the input is "M", or Float.NaN if invalid
     */
    private float validateNumber(String num, float memory) {
        float parsedNum = Float.NaN;
        if (VALID_INT_OR_FLOAT.matcher(num).matches()) { parsedNum = Float.parseFloat(num); }
        if (num.equals("M")) { parsedNum = memory; }
        return parsedNum;
    }

    private float evaluateEquation(float x, float y, String operator) {
        return switch (operator) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*" -> x * y;
            case "/" -> y == 0
                        ? Float.NaN
                        : x / y;
            default -> Float.NaN;
        };
    }
}
