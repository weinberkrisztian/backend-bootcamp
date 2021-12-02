package lambda.functionalInterfaces.supplier;

import data.Student;
import data.StudentDataBase;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Suppliers {

    public static Supplier<List<Student>> studentsSupplier = () -> StudentDataBase.getAllStudents();
    public static Supplier<List<Student>> studentsSupplierMethodRef = StudentDataBase :: getAllStudents;
    public static Consumer<Student> printStudent = stud -> System.out.println(stud);

    public static void main(String[] args) {
        List<Student> students = studentsSupplier.get();
        students.forEach(printStudent);
        List<Student> studentsMethodRef = studentsSupplierMethodRef.get();
        studentsMethodRef.forEach(printStudent);
    }
}
