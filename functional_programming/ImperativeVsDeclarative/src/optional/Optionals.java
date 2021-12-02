package optional;

import data.Student;
import data.StudentDataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Optionals {

    public static void main(String[] args) {
        ofNullable();
        mapOptional();
        orElseOptionals();
    }

    private static void ofNullable(){
        Optional<Object> nullValue = Optional.ofNullable(null);
        if(nullValue.isPresent()){
            System.out.println(nullValue.get());
        }

        Optional<String> optionalString = Optional.ofNullable("Hello Optional");
        if(optionalString.isPresent()){
            System.out.println(optionalString.get());
        }
    }

    private static void mapOptional(){
        Optional.ofNullable(123).ifPresent(number -> System.out.println(number));
        Optional.ofNullable(StudentDataBase.singleStudentSupplier.get()).map(Student::getName).ifPresent(System.out::println);

    }

    private static void orElseOptionals(){
        String optionalNameByMap = Optional.ofNullable(StudentDataBase.singleStudentSupplier.get()).map(Student::getName).orElse("orELseName");
        System.out.println(optionalNameByMap);
        String nullString = null;
        String optionalNameByNull = Optional.ofNullable(nullString).map(String::toUpperCase).orElse("orELseName");
        System.out.println(optionalNameByNull);
        String optionalSupplier = Optional.ofNullable(nullString).map(String::toUpperCase).orElseGet(() -> "orElseGetSupplier");
        System.out.println(optionalSupplier);
        List<Student> studentsNullList = null;
        Optional.ofNullable(StudentDataBase.getAllStudents()).filter(students -> students.size() > 1)
                .map(students -> students.stream().map(Student::getName).collect(Collectors.toList())).ifPresent(System.out::println);
        Optional.ofNullable(studentsNullList).filter(students -> students.size() > 1).ifPresentOrElse(students -> System.out.println("run"),
                () -> System.out.println("orElse run"));
    }
}
