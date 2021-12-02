import java.util.stream.IntStream;

public class Sum {
    public static void main(String[] args) {
        /**
         * Impreative : Focuses on how it is done
         * declares the steps
         * mutates the object
         * harder to read
         */
        int sumImperative = 0;
        for (int i = 0; i <= 100 ; i++) {
            sumImperative += i;
        }
        System.out.println("Impreative sumImperative: " + sumImperative);

        /**
         * Declarative : Focuses on what we want to achive
         * does not mutate the object
         * does not matter how we get the reasources
         * easier to read
         */
//        int sumDeclarative = IntStream.rangeClosed(0, 100).sum();
        int sumDeclarative2 = IntStream.range(0,100).sum();
        System.out.println("Declarative sum: " + sumDeclarative2);
    }
}
