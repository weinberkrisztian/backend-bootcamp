package lambda.functionalInterfaces.consumer;

import data.Student;
import data.StudentDataBase;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Consumers {

    private static void printStudentName(){
        List<Student> students = StudentDataBase.getAllStudents();
        students.forEach(student -> System.out.println(student));
    }

    private static void multiplicationBiConsumer(){
        BiConsumer<Integer, Integer> mult1 = ((integer, integer2) -> System.out.println("FirstMultiplication: " +(integer * integer2)));
        BiConsumer<Integer, Integer> mult2 = ((integer, integer2) -> System.out.println("SecondMultiplication: " +(integer * integer2 * 2)));
        mult1.andThen(mult2).accept(5,6);

    }

    private static void printNameAndActivities(){
        List<Student> students = StudentDataBase.getAllStudents();
        Consumer<Student> printName = student -> System.out.println(" Name: " + student.getName());
        Consumer<Student> printActivities =  student -> System.out.println(" Activities: " + student.getActivities());
        students.forEach(printName.andThen(printActivities).andThen(printName));
    }

    public static void main(String[] args) {
        Consumer<String> c1 = s -> System.out.println(s);
        c1.accept("println logic");

        printStudentName();
        printNameAndActivities();
        multiplicationBiConsumer();
    }
}
