package tracker;

import java.util.Scanner;

public class CommandProcessor {
    private final StudentManager studentManager = new StudentManager();
    private final Scanner scanner = new Scanner(System.in);

    public void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "add students":
                addStudents();
                break;
            case "list":
                listStudents();
                break;
            case "add points":
                addPoints();
                break;
            case "find":
                findStudent();
                break;
            case "statistics":
                studentManager.calculateAndDisplayStatistics();
                handleCourseDetails();
                break;
            case "notify":
                studentManager.generateNotifications();
                break;
            case "back":
                System.out.println("Enter 'exit' to exit the program.");
                break;
            default:
                System.out.println("Error: unknown command!");
        }
    }

    private void addStudents() {
        System.out.println("Enter student credentials or 'back' to return:");
        while (true) {
            String input = scanner.nextLine().trim();
            if ("back".equalsIgnoreCase(input)) {
                System.out.println("Total " + studentManager.getStudentCount() + " students have been added.");
                return;
            } else if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Incorrect credentials.");
            } else {
                studentManager.addStudent(input);
            }
        }
    }

    private void listStudents() {
        studentManager.listAllStudents();
    }

    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return:");
        while (true) {
            String input = scanner.nextLine().trim();
            if ("back".equalsIgnoreCase(input)) {
                return;
            }
            studentManager.addPointsToStudent(input);
        }
    }

    private void findStudent() {
        System.out.println("Enter an id or 'back' to return:");
        while (true) {
            String input = scanner.nextLine().trim();
            if ("back".equalsIgnoreCase(input)) {
                return;
            }
            studentManager.findStudentById(input);
        }
    }

    private void handleCourseDetails() {
        String input;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("back")) {
            if (!studentManager.displayCourseDetails(input)) {
                System.out.println("Unknown course.");
            }
        }
    }

}
