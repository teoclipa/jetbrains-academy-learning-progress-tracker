# Learning Progress Tracker

The Learning Progress Tracker is a Java application designed to manage and track the progress of students in online courses. Developed as a versatile tool, it caters to educational platforms offering courses in Java, Data Structures and Algorithms (DSA), Databases, and Spring. The application provides functionalities such as student registration, point allocation, comprehensive statistical analysis, and automated completion notifications. 

## Features

1. **Student Registration**: Add new students to the system with their credentials, including full name and email.

2. **Point Allocation**: Allocate points to students for their coursework in the four available courses.

3. **Statistical Analysis**:
   - Determine the most and least popular courses based on enrollment.
   - Identify courses with the highest and lowest student activity.
   - Calculate the easiest and hardest courses based on the average score per assignment.
   - Generate a leaderboard for top learners in each course.

4. **Completion Notifications**: Automatically send personalized completion notifications to students who have finished a course, ensuring each student receives only one notification per course completion.

5. **Data Handling**: Efficient management and retrieval of student data, course statistics, and completion records.

## Usage

- To add a new student: Enter the command `add students` and provide the student's credentials.
- To list all students: Use the command `list`.
- To add points for a student: Use the command `add points` followed by the student's ID and points for each course.
- To view statistics: Enter `statistics` to view course-wise statistics and top learners.
- To notify students of course completion: Use the command `notify`.
