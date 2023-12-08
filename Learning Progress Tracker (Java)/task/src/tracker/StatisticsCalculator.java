package tracker;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsCalculator {
    private final Map<Integer, Student> studentData;

    public StatisticsCalculator(Map<Integer, Student> studentData) {
        this.studentData = studentData;
    }

    public static int getCompletionThresholdByCourseName(String courseName) {
        return switch (courseName) {
            case "Java" -> CourseConstants.JAVA_COMPLETION_THRESHOLD;
            case "DSA" -> CourseConstants.DSA_COMPLETION_THRESHOLD;
            case "Databases" -> CourseConstants.DATABASES_COMPLETION_THRESHOLD;
            case "Spring" -> CourseConstants.SPRING_COMPLETION_THRESHOLD;
            default -> -1;
        };
    }

    public String calculateMostPopularCourse() {
        Map<String, Long> courseEnrollmentCount = calculateCourseEnrollment();
        if (courseEnrollmentCount.isEmpty()) {
            return "n/a";
        }

        long maxEnrollment = Collections.max(courseEnrollmentCount.values());
        return courseEnrollmentCount.entrySet().stream().filter(entry -> entry.getValue().equals(maxEnrollment)).map(Map.Entry::getKey).sorted().collect(Collectors.joining(", "));
    }

    public String calculateLeastPopularCourse() {
        Map<String, Long> courseEnrollmentCount = calculateCourseEnrollment();
        if (courseEnrollmentCount.isEmpty()) {
            return "n/a";
        }

        long minEnrollment = Collections.min(courseEnrollmentCount.values());
        if (minEnrollment == Collections.max(courseEnrollmentCount.values())) {
            return "n/a";
        }

        return courseEnrollmentCount.entrySet().stream().filter(entry -> entry.getValue().equals(minEnrollment)).map(Map.Entry::getKey).sorted().collect(Collectors.joining(", "));
    }


    private Map<String, Long> calculateCourseEnrollment() {
        Map<String, Long> courseEnrollmentCount = initializeCourseCountMap();
        studentData.values().forEach(student -> {
            for (int i = 0; i < student.getPoints().length; i++) {
                if (student.getPoints()[i] > 0) {
                    String courseName = getCourseNameByIndex(i);
                    courseEnrollmentCount.put(courseName, courseEnrollmentCount.get(courseName) + 1);
                }
            }
        });

        if (courseEnrollmentCount.values().stream().allMatch(count -> count == 0)) {
            return Collections.emptyMap();
        }

        return courseEnrollmentCount;
    }

    private Map<String, Long> calculateCourseActivity() {
        Map<String, Long> courseActivity = initializeCourseCountMap();
        studentData.values().forEach(student -> {
            for (int i = 0; i < student.getPoints().length; i++) {
                if (student.getPoints()[i] > 0) {
                    // Each non-zero point in a course is considered as one activity (submission)
                    String courseName = getCourseNameByIndex(i);
                    courseActivity.put(courseName, courseActivity.get(courseName) + 1);
                }
            }
        });

        if (courseActivity.values().stream().allMatch(count -> count == 0)) {
            return Collections.emptyMap();
        }

        return courseActivity;
    }

    private Map<String, Double> calculateCourseDifficulty() {
        Map<String, Double> courseDifficulty = new HashMap<>();
        Map<String, Integer> courseEnrollment = new HashMap<>();
        initializeCourseMaps(courseDifficulty, courseEnrollment);

        studentData.values().forEach(student -> {
            for (int i = 0; i < student.getPoints().length; i++) {
                if (student.getPoints()[i] > 0) {
                    String courseName = getCourseNameByIndex(i);
                    double currentDifficulty = courseDifficulty.get(courseName);
                    int currentEnrollment = courseEnrollment.get(courseName);
                    courseDifficulty.put(courseName, currentDifficulty + student.getPoints()[i]);
                    courseEnrollment.put(courseName, currentEnrollment + 1);
                }
            }
        });

        if (courseEnrollment.values().stream().allMatch(count -> count == 0)) {
            return Collections.emptyMap(); // Return empty map if no students enrolled
        }

        courseDifficulty.forEach((course, totalPoints) -> {
            int enrollments = courseEnrollment.get(course);
            if (enrollments > 0) {
                courseDifficulty.put(course, totalPoints / enrollments);
            } else {
                courseDifficulty.put(course, 0.0);
            }
        });

        return courseDifficulty;
    }

    public String calculateHighestActivityCourse() {
        Map<String, Long> courseActivity = calculateCourseActivity();

        if (courseActivity.isEmpty()) {
            return "n/a";
        }

        long maxActivity = Collections.max(courseActivity.values());
        List<String> coursesWithMaxActivity = courseActivity.entrySet().stream().filter(entry -> entry.getValue().equals(maxActivity)).map(Map.Entry::getKey).sorted().collect(Collectors.toList());

        return String.join(", ", coursesWithMaxActivity);
    }


    // Method to calculate the lowest activity course
    public String calculateLowestActivityCourse() {
        Map<String, Long> courseActivity = calculateCourseActivity();
        if (courseActivity.isEmpty()) {
            return "n/a";
        }

        long minActivity = Collections.min(courseActivity.values());
        if (minActivity == Collections.max(courseActivity.values())) {
            return "n/a";
        }

        return courseActivity.entrySet().stream().filter(entry -> entry.getValue().equals(minActivity)).map(Map.Entry::getKey).sorted().collect(Collectors.joining(", "));
    }

    public String calculateEasiestCourse() {
        Map<String, Double> courseDifficulty = calculateCourseDifficulty();
        if (courseDifficulty.isEmpty()) {
            return "n/a";
        }
        return courseDifficulty.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("n/a");
    }

    public String calculateHardestCourse() {
        Map<String, Double> courseDifficulty = calculateCourseDifficulty();
        if (courseDifficulty.isEmpty()) {
            return "n/a";
        }
        return courseDifficulty.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("n/a");
    }

    private void initializeCourseMaps(Map<String, Double> courseDifficulty, Map<String, Integer> courseEnrollment) {
        Arrays.asList(CourseConstants.JAVA, CourseConstants.DSA, CourseConstants.DATABASES, CourseConstants.SPRING).forEach(course -> {
            courseDifficulty.put(course, 0.0);
            courseEnrollment.put(course, 0);
        });
    }

    private Map<String, Long> initializeCourseCountMap() {
        Map<String, Long> courseCountMap = new HashMap<>();
        List<String> courses = Arrays.asList(CourseConstants.JAVA, CourseConstants.DSA, CourseConstants.DATABASES, CourseConstants.SPRING);
        courses.forEach(course -> courseCountMap.put(course, 0L));
        return courseCountMap;
    }


    public Map<String, List<Student>> calculateTopLearnersPerCourse() {
        Map<String, List<Student>> topLearnersPerCourse = new HashMap<>();
        Arrays.asList(CourseConstants.JAVA, CourseConstants.DSA, CourseConstants.DATABASES, CourseConstants.SPRING).forEach(course -> topLearnersPerCourse.put(course, new ArrayList<>()));

        studentData.values().forEach(student -> {
            for (int i = 0; i < student.getPoints().length; i++) {
                if (student.getPoints()[i] > 0) {
                    String courseName = getCourseNameByIndex(i);
                    topLearnersPerCourse.get(courseName).add(student);
                }
            }
        });

        topLearnersPerCourse.forEach((course, students) -> students.sort(Comparator.comparingInt((Student s) -> s.getPoints()[getCourseIndexByName(course)]).reversed().thenComparingInt(Student::getId)));

        return topLearnersPerCourse;
    }


    static int getCourseIndexByName(String course) {
        return switch (course) {
            case "Java" -> 0;
            case "DSA" -> 1;
            case "Databases" -> 2;
            case "Spring" -> 3;
            default -> -1;
        };
    }

    private String getCourseNameByIndex(int index) {
        return switch (index) {
            case 0 -> "Java";
            case 1 -> "DSA";
            case 2 -> "Databases";
            case 3 -> "Spring";
            default -> "Unknown";
        };
    }

}
