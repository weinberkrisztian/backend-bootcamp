package lambda.functionalInterfaces.custom;

@FunctionalInterface
public interface Growl<T> {
    T growling(T sound);
}
