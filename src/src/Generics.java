package src;
/* inspired from: https://softwareengineering.stackexchange.com/questions/262706/why-liste-is-an-interface-but-not-abstract-class and
   https://www.digitalocean.com/community/tutorials/java-generics-example-method-class-interface
*/
public class Generics {
    public static void main(String... args) {
        MyGenericType<String> ex1 = new MyGenericType<>();
        ex1.set("ex1 object string");
        System.out.println((String)ex1.getValue());  // type-casting to the specific object (without it defaults)
    }
    
}

/**
 * Example 1
 * Define a class with a generic type. This type can be a
 * class/interface that is parameterized over types.
 */
class MyGenericType<T> implements MyGenericInterface<T> {
    private T t;

    public T getValue() {
        return t;
    }

    public void set(T o) {
        this.t = o;
    }
}

/**
 * Example 2
 * Generic Interface - implemented above
 * The benefit of this is that now if we have any number of complex
 * objects that may not be related, having this interface
 * implemented in them means that that FUNCTIONALITY is guaranteed in
 * those objects. 
 */
interface MyGenericInterface<T> {
    T getValue();
}