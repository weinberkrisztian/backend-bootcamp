import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Duplication {
    public static void main(String[] args) {
        List<Integer> integerList = List.of(1,2,3,3,3,5,7,7,8,9,9);

        /**
         * Imperative
         */
        List<Integer> uniqueListImp = new ArrayList<>();
        for (Integer integer : integerList){
            if(!uniqueListImp.contains(integer)){
                uniqueListImp.add(integer);
            }
        }
        System.out.println(uniqueListImp);

        /**
         * declaratie
         */
        List<Integer> uniqueListDecl = integerList.stream().distinct().collect(Collectors.toList());
        System.out.println(uniqueListDecl);
    }
}
