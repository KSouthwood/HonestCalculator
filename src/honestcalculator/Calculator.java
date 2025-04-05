package honestcalculator;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Calculator {
    private final        Map<Integer, String> messages    = Map.of(
            0, "Enter an equation",
            1, "Do you even know what numbers are? Stay focused!",
            2, "Yes ... an interesting math operation. You've slept through all classes, haven't you?",
            3, "Yeah... division by zero. Smart move..."
    );
    private static final List<String>         OPERATORS   = List.of("+", "-", "*", "/");
    private final        ConsoleIO            consoleIO;
    private static final Pattern              INT_REGEX   = Pattern.compile("^[-]?(\\d|[1-9]\\d*)$");
    private static final Pattern              FLOAT_REGEX = Pattern.compile("^[-]?\\d+(\\.\\d+)?$");

    public Calculator(ConsoleIO consoleIO) {
        this.consoleIO = consoleIO;
    }

    public void run() {
        boolean isValidEquation = false;
        do {
            String   calc     = getEquation();
            String[] equation = calc.split(" ");
            if (equation.length != 3) {
                continue;
            }
            if (validateNumbers(equation[0], equation[2])) {
                if (OPERATORS.contains(equation[1])) {
                    isValidEquation = evaluateEquation(equation);
                } else {
                    consoleIO.println(messages.get(2));
                }
            } else {
                consoleIO.println(messages.get(1));
            }
        } while (!isValidEquation);
    }

    private String getEquation() {
        consoleIO.println(messages.get(0));
        return consoleIO.getUserInput();
    }

    private boolean validateNumbers(String x, String y) {
        return validIntOrFloat(x) && validIntOrFloat(y);
    }

    private boolean validIntOrFloat(String num) {
        return INT_REGEX.matcher(num)
                        .matches() || FLOAT_REGEX.matcher(num)
                                                 .matches();
    }

    private boolean evaluateEquation(String[] equation) {
        float   x         = Float.parseFloat(equation[0]);
        float   y         = Float.parseFloat(equation[2]);
        boolean evaluated = true;

        switch (equation[1]) {
            case "+":
                consoleIO.println(x + y);
                break;
            case "-":
                consoleIO.println(x - y);
                break;
            case "*":
                consoleIO.println(x * y);
                break;
            case "/":
                if (y == 0) {
                    consoleIO.println(messages.get(3));
                    evaluated = false;
                } else {
                    consoleIO.println(x / y);
                }
                break;
            default:
        }

        return evaluated;
    }
}
