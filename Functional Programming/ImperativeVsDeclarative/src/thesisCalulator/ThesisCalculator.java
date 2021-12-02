package thesisCalulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThesisCalculator {

    List<Thesis> thesisesA;
    List<Thesis> thesisesB;
    Random r = new Random();

    public ThesisCalculator(List<String> listA, List<String> listB) {
        this.thesisesA = createThesisList(listA);
        this.thesisesB = createThesisList(listB);
    }

    private List<Thesis> createThesisList(List<String> thesisNames) {
        List<Thesis> thesises = new ArrayList<>();
        for (int thesisOrdinal = 0; thesisOrdinal < thesisNames.size(); thesisOrdinal++) {
            thesises.add(new Thesis(thesisNames.get(thesisOrdinal), thesisOrdinal + 1));
        }
        return thesises;
    }

    public boolean checkForNextThesis() {
        return (selectNextThesis(thesisesA) && selectNextThesis(thesisesB));
    }

    private boolean selectNextThesis(List<Thesis> thesises) {
        if (thesises.size() > 0) {
            printAndRemoveThesis(thesises);
            return true;
        }
        System.out.println("No more thesis!");
        return false;
    }

    private void printAndRemoveThesis(List<Thesis> thesis) {
        System.out.println(thesis.remove(r.nextInt(thesis.size())));
    }
}
