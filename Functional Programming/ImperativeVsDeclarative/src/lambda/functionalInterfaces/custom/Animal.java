package lambda.functionalInterfaces.custom;

public class Animal {

    public Growl<String> growl;

    public Animal(Growl<String> growl){
        this.growl = growl;
    }

    String growling(String sound){
        return this.growl.growling(sound);
    }
}
