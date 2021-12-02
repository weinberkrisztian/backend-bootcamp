package stream;

import data.Student;
import data.StudentDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.minBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Streams {
    public static void main(String[] args) {
        streamToMap();
        streamDistinct();
        streamCount();
        streamSort();
        streamCustomComparator();
        streamReduceMultiplication();
        streamReduceGetHighestGPAStudent();
        streamReduceSumStudentNoteBooks();
        streamReduceMaxMin();
        streamDifferentStreamTypes();
        streamBoxing();
        streamTerminalOperations();
    }

    private static void streamToMap() {
        // convert to map
        Map<String, List<String>> nameActivitiesMap = StudentDataBase.getAllStudents().stream()
                .filter(student -> student.getGradeLevel() >= 3)
                .collect(Collectors.toMap(Student::getName, Student::getActivities));
        System.out.println(nameActivitiesMap.toString());
        // convert to lists activities
        List<String> activities = StudentDataBase.getAllStudents().stream() // Stream<Student>
                .map(Student::getActivities) // Stream<List<String>>
                .flatMap(List::stream) // Stream<String>
                .collect(Collectors.toList());
        System.out.println(activities.toString());
    }

    private static void streamDistinct() {
        List<String> uniqueActivities = StudentDataBase.getAllStudents().stream() // Stream<Student>
                .map(Student::getActivities) // Stream<List<String>>
                .flatMap(List::stream) // Stream<String>
                .distinct()
                .collect(toList());
        System.out.println("DISTINCT " + uniqueActivities.toString());
    }

    private static void streamCount() {
        long activitiesCount = StudentDataBase.getAllStudents().stream() // Stream<Student>
                .map(Student::getActivities) // Stream<List<String>>
                .flatMap(List::stream) // Stream<String>
                .count();
        System.out.println("COUNT " + activitiesCount);
    }

    private static void streamSort() {
        List<String> sortedActivities = StudentDataBase.getAllStudents().stream() // Stream<Student>
                .map(Student::getActivities) // Stream<List<String>>
                .flatMap(List::stream) // Stream<String>
                .sorted().collect(toList());
        System.out.println("SORT " + sortedActivities);
    }

    private static void streamCustomComparator() {
        StudentDataBase.getAllStudents().stream().sorted(Comparator.comparing(Student::getName).reversed()).forEach(System.out::println);
    }

    private static void streamReduceMultiplication(){
        System.out.println(StudentDataBase.getAllStudents().stream().map(Student::getGradeLevel).reduce(1, (a,b) -> a*b));
    }

    private static void streamReduceGetHighestGPAStudent(){
        Optional<Student> highestGpaStudent = StudentDataBase.getAllStudents().stream().reduce((stud1, stud2) -> stud1.getGpa() > stud2.getGpa() ? stud1 : stud2);
        if(highestGpaStudent.isPresent()){
            System.out.println(highestGpaStudent.get());
        }
    }

    private static void streamReduceSumStudentNoteBooks(){
        Optional<Integer> noteBookCount = StudentDataBase.getAllStudents().stream().map(Student::getNoteBooks)
//                .reduce((nb1, nb2) -> nb1 + nb2);
                .reduce(Integer::sum);
        noteBookCount.ifPresent(count -> System.out.println(noteBookCount));
    }

    private static void streamReduceMaxMin(){
        // max
        StudentDataBase.getAllStudents().stream().reduce((stud1, stud2) -> stud1.getNoteBooks() > stud2.getNoteBooks() ? stud1 : stud2).ifPresent(System.out::println);
        // min
        StudentDataBase.getAllStudents().stream().reduce((stud1, stud2) -> stud1.getNoteBooks() < stud2.getNoteBooks() ? stud1 : stud2).ifPresent(System.out::println);
    }

    // FACTORY
    private static void streamDifferentStreamTypes(){
        System.out.println(IntStream.rangeClosed(1,6).sum());
        System.out.println(IntStream.rangeClosed(1,6).average());
        System.out.println(IntStream.rangeClosed(1,6).max());
        System.out.println(IntStream.rangeClosed(1,6).min());
        System.out.println(IntStream.rangeClosed(1,6).count());
        IntStream.rangeClosed(1,6).forEach(System.out::print);
        OptionalDouble optDoubleMin = IntStream.rangeClosed(1, 6).asDoubleStream().min();
        optDoubleMin.ifPresent(System.out::println);
    }

    private static void streamBoxing(){
        List<Integer> integersFromStream = IntStream.rangeClosed(1, 3)
                // int
                .boxed()
                // Integer
                .collect(toList());
        integersFromStream.forEach(System.out::print);

        // unBoxing
        List<Integer> integersFromList1 = List.of(1, 2, 3, 4, 5);
        int[] intArray = integersFromList1.stream().mapToInt(Integer::intValue)
                .toArray();
        Arrays.stream(intArray).forEach(System.out::println);
        List<Integer> integersFromList2 = List.of(1, 2, 3, 4, 5);
        // mapToDouble converts the IntStream to DoubleStream
        DoubleStream doubleStream = integersFromList2.stream().mapToDouble(value -> value);
        List<Double> doubleObjectList = doubleStream.boxed().collect(toList());
    }

    private static void streamTerminalOperations(){
        List<Student> students = StudentDataBase.getAllStudents();
        // join
        System.out.println(students.stream().map(Student::getName).collect(Collectors.joining("-", "(", ")")));

        // counting
        students = StudentDataBase.getAllStudents();
        System.out.println(students.stream().collect(counting()));

        // mapping
        students = StudentDataBase.getAllStudents();
        System.out.println(students.stream().collect(mapping(Student::getName, toMap(o -> o.concat("_key"), o -> o.concat("_value")))));

        // toMap
        students = StudentDataBase.getAllStudents();
        System.out.println(students.stream().collect(toMap(Student::getName, Student::getGpa)));

        // maxBy
        students = StudentDataBase.getAllStudents();
        System.out.println(students.stream().collect(maxBy(Comparator.comparing(Student::getGpa))));
        System.out.println(students.stream().collect(minBy(Comparator.comparing(Student::getGpa))));

        // sum, avrg
        students = StudentDataBase.getAllStudents();
        System.out.println(students.stream().collect(summingDouble(Student::getGpa)));
        System.out.println(students.stream().collect(averagingDouble(Student::getGpa)));

        // group by
        students = StudentDataBase.getAllStudents();
        Map<String, List<Student>> studentMapByGender = students.stream().collect(groupingBy(Student::getGender));
        System.out.println(studentMapByGender);
        Map<String, List<Student>> studensByGpaLevels = students.stream().collect(groupingBy(stud -> stud.getGpa() > 3.7 ? "OUTSTANDING" : "AVERAGE"));
        System.out.println(studensByGpaLevels);

        Map<String, Map<String, List<Student>>> groupByGpaAndGender = students.stream().collect(groupingBy(stud
                -> stud.getGpa() > 3.7 ? "OUTSTANDING" : "AVERAGE", groupingBy(Student::getGender)));
        System.out.println(groupByGpaAndGender);

        Map<String, Double> groupByGpaSum = students.stream().collect(groupingBy(stud
                -> stud.getGpa() > 3.7 ? "OUTSTANDING" : "AVERAGE", summingDouble(Student::getGpa)));
        System.out.println(groupByGpaSum);

        LinkedHashMap<String, Set<Student>> groupByNameHashMap = students.stream().collect(groupingBy(Student::getName, LinkedHashMap::new, toSet()));
        System.out.println(groupByNameHashMap);

        LinkedHashMap<String, Double> gpaSumGroupByNameLinkedHashMap = students.stream().collect(groupingBy(Student::getName, LinkedHashMap::new, summingDouble(Student::getGpa)));
        System.out.println(gpaSumGroupByNameLinkedHashMap);

        LinkedHashMap<String, Double> studGenderGpaSumLinkedHM = students.stream().collect(groupingBy(Student::getGender, LinkedHashMap::new, summingDouble(Student::getGpa)));
        System.out.println(studGenderGpaSumLinkedHM);

        Map<Integer, Optional<Student>> maxGpaByGradeLevel = students.stream().collect(groupingBy(Student::getGradeLevel, maxBy(Comparator.comparing(Student::getGpa))));
        System.out.println(maxGpaByGradeLevel);

        Map<Integer, Student> maxGpaByGradeLevelOptionalGet = students.stream().collect(groupingBy(Student::getGradeLevel, collectingAndThen(maxBy(Comparator.comparing(Student::getGpa)), Optional::get)));
        System.out.println(maxGpaByGradeLevelOptionalGet);

        // partitioning
        Map<Boolean, List<Student>> gpaPartition = students.stream().collect(partitioningBy(stud -> stud.getGpa() > 3.7));
        System.out.println(gpaPartition);
        System.out.println(gpaPartition.get(true));
        Map<Boolean, Set<Student>> gpaPartitionSet = students.stream().collect(partitioningBy(stud -> stud.getGpa() > 3.7, toSet()));
        System.out.println(gpaPartitionSet);
        System.out.println(gpaPartitionSet.get(true));
    }
}
