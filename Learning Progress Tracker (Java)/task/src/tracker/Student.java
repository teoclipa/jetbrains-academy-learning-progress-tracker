package tracker;

import java.util.HashSet;
import java.util.Set;

public class Student {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int[] points; // Points for Java, DSA, Databases, Spring

    public Student(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.points = new int[4]; // Initialize points array
    }

    public void addPoints(int[] newPoints) {
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] += newPoints[i];
        }
    }

    public boolean isEnrolledInCourse(int courseIndex) {
        return points[courseIndex] > 0;
    }

    public int getTotalPointsForCourse(int courseIndex) {
        return points[courseIndex];
    }

    public int getId() {
        return id;
    }

    public int[] getPoints() {
        return points;
    }

    private final Set<String> notifiedCourses = new HashSet<>();

    public boolean hasCompletedCourse(String courseName) {
        int index = StatisticsCalculator.getCourseIndexByName(courseName);
        int completionThreshold = StatisticsCalculator.getCompletionThresholdByCourseName(courseName);
        return this.points[index] >= completionThreshold;
    }

    public boolean isNotifiedForCourse(String courseName) {
        return notifiedCourses.contains(courseName);
    }

    public void markNotifiedForCourse(String courseName) {
        notifiedCourses.add(courseName);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d",
                id, points[0], points[1], points[2], points[3]);
    }

    public String getEmail() {
        return email;
    }
}
