package honestcalculator;

import java.util.List;
import java.util.Map;

public class Calculator {
    private final Map<Integer, String> messages = Map.of(
            0, "Enter an equation",
            1, "Do you even know what numbers are? Stay focused!",
            2, "Yes ... an interesting math operation. You've slept through all classes, haven't you?"
    );
    private static final List<String> operators = List.of("+", "-", "*", "/");
    private final        ConsoleIO    consoleIO;

    public Calculator(ConsoleIO consoleIO) {
        this.consoleIO = consoleIO;
    }
    public void run() {
        boolean isValidEquation = false;
        do {
            String calc = getEquation();
            String[] equation = calc.split(" ");
            if (equation.length != 3) {
                continue;
            }
            if (validateNumbers(equation[0], equation[2])) {
                if (operators.contains(equation[1])) {
                    isValidEquation = true;
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
        try {
            float xNum = Float.parseFloat(x);
            float yNum = Float.parseFloat(y);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
