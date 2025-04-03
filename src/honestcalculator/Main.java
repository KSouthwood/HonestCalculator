package honestcalculator;

public class Main {
    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO(System.in, System.out);
        Calculator calculator = new Calculator(consoleIO);
        calculator.run();
    }
}
