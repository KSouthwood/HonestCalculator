package honestcalculator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Calculator {
    private final        Map<Integer, String> messages            = Map.ofEntries(
            Map.entry(0, "Enter an equation"),
            Map.entry(1, "Do you even know what numbers are? Stay focused!"),
            Map.entry(2, "Yes ... an interesting math operation. You've slept through all classes, haven't you?"),
            Map.entry(3, "Yeah... division by zero. Smart move..."),
            Map.entry(4, "Do you want to store the result? (y / n):"),
            Map.entry(5, "Do you want to continue calculations? (y / n):"),
            Map.entry(6, " ... lazy"),
            Map.entry(7, " ... very lazy"),
            Map.entry(8, " ... very, very lazy"),
            Map.entry(9, "You are"),
            Map.entry(10, "Are you sure? It is only one digit! (y / n)"),
            Map.entry(11, "Don't be silly! It's just one number! Add to the memory? (y / n)"),
            Map.entry(12, "Last chance! Do you really want to embarrass yourself? (y / n)")
    );
    private final        ConsoleIO            consoleIO;
    private static final Pattern              VALID_INT_OR_DOUBLE = Pattern.compile("^-?(\\d|[1-9]\\d*)(\\.\\d+)?$");
    private static final List<String>         OPERATORS           = List.of("+", "-", "*", "/");
    private static final List<String>         YES_OR_NO           = List.of("y", "n");

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

            if (askToStoreResult(result)) {
                memory = result;
            }

            isFinishedCalculations = checkContinueCalculations();

        } while (!isFinishedCalculations);
    }

    private boolean askToStoreResult(double result) {
        String  answer;
        boolean storeResult = true;

        do {
            consoleIO.println(messages.get(4));
            answer = consoleIO.getUserInput()
                              .toLowerCase(Locale.ROOT);
            switch (answer) {
                case "y" -> {
                    if (isOneDigit(result)) {
                        storeResult = storeResult();
                    }
                }
                case "n" -> storeResult = false;
            }
        } while (!YES_OR_NO.contains(answer));

        return storeResult;
    }

    private boolean storeResult() {
        String  answer      = "";
        int     msgIndex    = 10;
        boolean storeResult = true;

        while (msgIndex <= 12 && !answer.equals("n")) {
            consoleIO.println(messages.get(msgIndex));
            answer = consoleIO.getUserInput()
                              .toLowerCase(Locale.ROOT);
            switch (answer) {
                case "y" -> msgIndex++;
                case "n" -> storeResult = false;
            }
        }

        return storeResult;
    }

    private boolean checkContinueCalculations() {
        String  answer;
        boolean continueCalculations = false;

        do {
            consoleIO.println(messages.get(5));
            answer = consoleIO.getUserInput()
                              .toLowerCase(Locale.ROOT);
            switch (answer) {
                case "y" -> continueCalculations = false;
                case "n" -> continueCalculations = true;
            }
        } while (!YES_OR_NO.contains(answer));

        return continueCalculations;
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
