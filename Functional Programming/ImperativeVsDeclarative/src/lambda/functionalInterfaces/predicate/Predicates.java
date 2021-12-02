package lambda.functionalInterfaces.predicate;

import data.Student;
import data.StudentDataBase;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Predicates {

    public static Predicate<Integer> divisibleBy2 = i -> i%2==0;
    public static Predicate<Integer> divisibleBy5 = i -> i%5==0;
    public static Predicate<Integer> divisibleBy7 = i -> i%7==0;
    public static Predicate<Student> studentGradeLargerThanThree = student -> student.getGradeLevel()>=3;
    public static BiPredicate<Student, Integer> biPred= (stud, i) -> stud.getGradeLevel() > i;
    public static List<Student> students = StudentDataBase.getAllStudents();
    public static void main(String[] args) {
        basicExamples();
        combinePredicateAndConsumer();
        biPredicateExample();
    }

    private static void combinePredicateAndConsumer(){
        students.forEach(student -> {
            if(studentGradeLargerThanThree.test(student)){
                System.out.println("Pred " + student);
            }
        });
    }

    private static void biPredicateExample(){
        students.forEach(student -> {
            if(biPred.test(student,3)){
                System.out.println("BiPred " + student);
            }
        });
    }

    private static void basicExamples(){
        System.out.println(divisibleBy2.and(divisibleBy5).test(20));
        System.out.println(divisibleBy2.and(divisibleBy5).or(divisibleBy7).test(35));
        System.out.println(divisibleBy2.and(divisibleBy5).negate().and(divisibleBy7).test(35));
    }
}
