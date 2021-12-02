package thesisCalulator;

public class Thesis {

    private String name;
    private int ordinal;

    public Thesis(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return "Thesis{" +
                "name='" + name + '\'' +
                ", ordinal=" + ordinal +
                '}';
    }
}
