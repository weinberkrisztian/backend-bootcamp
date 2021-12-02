package lambda.functionalInterfaces.custom;

public class Main {
    public static void main(String[] args) {
        Animal dog = new Animal(String :: toUpperCase);
        System.out.println(dog.growling("vau-vau"));
    }
}
