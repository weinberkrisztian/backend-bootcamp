package thesisCalulator;

import java.util.List;
import java.util.Scanner;

public class ThesisRunner {
    public static void main(String[] args) {
        ThesisCalculator thesisCalc = new ThesisCalculator(List.of("FirstA", "SecondA", "ThirdA"), List.of("FirstB", "SecondB", "ThirdB"));

        Scanner sc = new Scanner(System.in);
        boolean go = true;
        String input;
        while (go){
            input = sc.nextLine();
            if(input.equals("next")){
                go = thesisCalc.checkForNextThesis();
            }else{
                break;
            }
        }
    }
}
