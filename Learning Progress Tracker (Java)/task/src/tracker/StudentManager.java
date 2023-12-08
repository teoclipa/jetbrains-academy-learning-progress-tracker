package tracker;

import java.util.*;

public class StudentManager {
    private final Map<Integer, Student> studentData = new LinkedHashMap<>();
    private final Map<String, Integer> emailToId = new HashMap<>();
    private int studentStartId = 1000;

    public void addStudent(String credentials) {
        String[] parts = credentials.trim().split("\\s+");
        if (parts.length < 3) {
            System.out.println("Incorrect credentials.");
            return;
        }

        String firstName = parts[0];
        String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));
        String email = parts[parts.length - 1];

        if (isNotValidName(firstName)) {
            System.out.println("Incorrect first name.");
        } else if (isNotValidName(lastName)) {
            System.out.println("Incorrect last name.");
        } else if (!isValidEmail(email)) {
            System.out.println("Incorrect email.");
        } else if (emailToId.containsKey(email)) {
            System.out.println("This email is already taken.");
        } else {
            int id = studentStartId++;
            Student newStudent = new Student(id, firstName, lastName, email);
            studentData.put(id, newStudent);
            emailToId.put(email, id);
            System.out.println("The student has been added.");
        }
    }

    public void listAllStudents() {
        if (studentData.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            studentData.keySet().forEach(System.out::println);
        }
    }

    public int getStudentCount() {
        return studentData.size();
    }

    public void addPointsToStudent(String inputData) {
        String[] parts = inputData.trim().split("\\s+");
        if (parts.length != 5) {
            System.out.println("Incorrect points format.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("No student is found for id=" + parts[0] + ".");
            return;
        }

        int[] points = new int[4];
        try {
            for (int i = 0; i < 4; i++) {
                points[i] = Integer.parseInt(parts[i + 1]);
                if (points[i] < 0) {
                    System.out.println("Incorrect points format.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Incorrect points format.");
            return;
        }

        Student student = studentData.get(id);
        if (student == null) {
            System.out.println("No student is found for id=" + id + ".");
            return; // ID does not exist
        }

        student.addPoints(points);
        System.out.println("Points updated."); // Points successfully updated
    }

    public void findStudentById(String input) {
        if (!input.matches("\\d+")) {
            System.out.println("Incorrect ID format.");
            return;
        }
        int id = Integer.parseInt(input);
        if (!studentData.containsKey(id)) {
            System.out.println("No student is found for id=" + id + ".");
            return;
        }
        System.out.println(studentData.get(id));
    }

    private boolean isNotValidName(String name) {
        String[] nameParts = name.split("\\s+");
        for (String part : nameParts) {
            if (!isValidNamePart(part)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidNamePart(String namePart) {
        return namePart.matches("[A-Za-z'-]{2,}") && !namePart.startsWith("-") && !namePart.startsWith("'") && !namePart.endsWith("-") && !namePart.endsWith("'") && !namePart.contains("''") && !namePart.contains("--") && !namePart.contains("'-") && !namePart.contains("-'");
    }

    private boolean isValidEmail(String email) {
        return email.matches("[\\w.-]+@[\\w-]+(\\.[\\w-]+)*\\.[\\w-]+");
    }

    public void calculateAndDisplayStatistics() {
        StatisticsCalculator statsCalculator = new StatisticsCalculator(studentData);

        System.out.println("Type the name of a course to see details or 'back' to quit:");
        System.out.println("Most popular: " + statsCalculator.calculateMostPopularCourse());
        System.out.println("Least popular: " + statsCalculator.calculateLeastPopularCourse());
        System.out.println("Highest activity: " + statsCalculator.calculateHighestActivityCourse());
        System.out.println("Lowest activity: " + statsCalculator.calculateLowestActivityCourse());
        System.out.println("Easiest course: " + statsCalculator.calculateEasiestCourse());
        System.out.println("Hardest course: " + statsCalculator.calculateHardestCourse());
    }


    public boolean displayCourseDetails(String courseName) {
        if (!Arrays.asList("Java", "DSA", "Databases", "Spring").contains(courseName)) {
            return false; // Invalid course name
        }

        StatisticsCalculator statsCalculator = new StatisticsCalculator(studentData);
        Map<String, List<Student>> topLearnersPerCourse = statsCalculator.calculateTopLearnersPerCourse();

        System.out.println(courseName);
        System.out.println("id\tpoints\tcompleted");
        topLearnersPerCourse.get(courseName).forEach(student -> {
            int totalPoints = student.getTotalPointsForCourse(StatisticsCalculator.getCourseIndexByName(courseName));
            double completionPercentage = calculateCompletionPercentage(totalPoints, courseName);
            System.out.printf("%d\t%d\t%.1f%%%n", student.getId(), totalPoints, completionPercentage);
        });

        return true;
    }

    public void generateNotifications() {
        Set<Integer> notifiedStudents = new HashSet<>();
        for (Student student : studentData.values()) {
            boolean studentNotified = false;
            for (String course : Arrays.asList("Java", "DSA", "Databases", "Spring")) {
                if (student.hasCompletedCourse(course) && !student.isNotifiedForCourse(course)) {
                    if (!studentNotified) {
                        studentNotified = true;
                        notifiedStudents.add(student.getId());
                    }
                    sendNotification(student, course);
                    student.markNotifiedForCourse(course);
                }
            }
        }
        System.out.println("Total " + notifiedStudents.size() + " students have been notified.");
    }

    private void sendNotification(Student student, String courseName) {
        System.out.printf("To: %s\nRe: Your Learning Progress\nHello, %s! You have accomplished our %s course!\n", student.getEmail(), student.getFullName(), courseName);
    }

    private double calculateCompletionPercentage(int totalPoints, String courseName) {
        int maxPoints = switch (courseName) {
            case "Java" -> 600;
            case "DSA" -> 400;
            case "Databases" -> 480;
            case "Spring" -> 550;
            default -> 0;
        };
        return (double) totalPoints / maxPoints * 100;
    }

}
