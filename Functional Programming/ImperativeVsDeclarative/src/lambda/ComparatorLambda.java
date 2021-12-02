package lambda;

import java.util.Comparator;

public class ComparatorLambda{

    public static void main(String[] args) {
        /**
         * prior java 8
         */

        Comparator<Integer> comp = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        Comparator<Integer> comparator = (o1, o2) -> o1.compareTo(o2);
        comparator.compare(1,2);
    }
}
