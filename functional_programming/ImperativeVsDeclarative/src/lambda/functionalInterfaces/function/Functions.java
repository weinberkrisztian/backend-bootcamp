package lambda.functionalInterfaces.function;

import data.Student;
import data.StudentDataBase;
import lambda.functionalInterfaces.predicate.Predicates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Functions {

    private static Function<String, String> toUpperCaseFunctin = s -> s.toUpperCase();
    private static Function<String, String> concatString = s -> s.concat("concatenatedString");
    private static Function<String, String> toUpperCaseFunctinMethRef = String :: toUpperCase;
    private static Function<String, String> concatStringFunctionMethRef = Functions :: concatStringMethod;

    public static void main(String[] args) {
        basicExapmle();
        studentFunction();
    }

    private static Function<List<Student>, Map<String, Double>> studToNameGpaMap = students -> {
        Map<String, Double> nameGpaMap = new HashMap<>();
        students.forEach(student -> {
            if(Predicates.studentGradeLargerThanThree.test(student)){
                nameGpaMap.put(student.getName(), student.getGpa());
            }
        });
        return nameGpaMap;
    };

    private static BiFunction<List<Student>, Predicate<Student>, Map<String, Double>> studToNameGpaMapBi = (students, pred) -> {
        Map<String, Double> nameGpaMap = new HashMap<>();
        students.forEach(student -> {
            if(pred.test(student)){
                nameGpaMap.put(student.getName(), student.getGpa());
            }
        });
        return nameGpaMap;
    };

    private static void studentFunction(){
        Map<String, Double> nameGpaMap = studToNameGpaMap.apply(StudentDataBase.getAllStudents());
        System.out.println(nameGpaMap.toString());
        System.out.println(studToNameGpaMapBi.apply(StudentDataBase.getAllStudents(), Predicates.studentGradeLargerThanThree).toString());
    }

    private static void basicExapmle(){
        System.out.println(toUpperCaseFunctin.apply("lowcaseletters"));
        System.out.println(toUpperCaseFunctinMethRef.apply("lowcaseletters"));
        System.out.println(concatStringFunctionMethRef.apply("methodReference"));
        System.out.println(toUpperCaseFunctin.andThen(concatString).apply("lowcaseletters"));
        System.out.println(toUpperCaseFunctin.compose(concatString).apply("lowcaseletters"));
        System.out.println(Function.identity());
    }

    private static String concatStringMethod(String s){
        String concatedStr = s.concat("ConcatForMethodReference");
        System.out.println(concatedStr);
        return concatedStr;
    }
}
