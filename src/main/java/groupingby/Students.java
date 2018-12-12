package groupingby;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Student {
    private String name;
    private double grade;

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }

    public static String getLetterGrade(double grade) {
        if (grade > 3.7) return "A";
        if (grade > 3.3) return "B";
        if (grade > 2.9) return "C";
        if (grade > 2.5) return "D";
        return "E";
    }
}

public class Students {
    public static void main(String[] args) {
        List<Student> school = Arrays.asList(
                new Student("Fred", 3.8),
                new Student("Jim", 2.8),
                new Student("Jimmy", 2.8),
                new Student("Sheila", 3.0)
        );

        Map<String, Long> res = school.stream()
                .collect(
                        Collectors.groupingBy(
                                (Student s) -> Student.getLetterGrade(s.getGrade()),
                                Collectors.counting())
                );
        System.out.println(res);

        school.stream()
                .collect(
                        Collectors.groupingBy(
                                (Student s) -> Student.getLetterGrade(s.getGrade()),
                                Collectors.mapping(s -> s.getName(), Collectors.toList()))
                )
                .entrySet().stream()
                .forEach(e -> System.out.printf("Grade %s was achieved by %s\n",
                        e.getKey(), e.getValue().stream().collect(Collectors.joining(", "))));
    }

}
