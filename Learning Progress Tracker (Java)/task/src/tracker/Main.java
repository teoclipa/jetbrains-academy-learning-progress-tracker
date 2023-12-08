package tracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        CommandProcessor commandProcessor = new CommandProcessor();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Bye!");
                break;
            } else if (input.isEmpty()) {
                System.out.println("No input.");
            } else {
                commandProcessor.processCommand(input);
            }
        }
    }
}
