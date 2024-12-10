package src;
public class Pass {
    public static void main(String[] args) {
        Dog aDog = new Dog("Max");
        Dog oldDog = aDog;
    
        // we pass the object to foo
        System.out.println("mem address before foo: " + aDog);
        foo(aDog);
        System.out.println("mem address after foo: " + aDog);
        // aDog variable is still pointing to the "Max" dog when foo(...) returns
        System.out.println(aDog.getName().equals("Max")); // true
        System.out.println(aDog.getName().equals("Fifi")); // false
        System.out.println(aDog == oldDog); // true
    }
    
    /**
     * In Java everything is an object and the VALUE of the memory address is passed. Any modication to that
     * object MUST be dont with object (getter/setter) methods or else the new memory will be lost indefinitly.
     * The only to svoid getter/setters is to return the new object (memory address) and set it to the pointer.
     */
    public static void foo(Dog d) {
        System.out.println("mem address inside foo: " + d);
        d.getName().equals("Max"); // true
        // change d inside of foo() to point to a new Dog instance construct red with name member variable set to "Fifi"
        d = new Dog("Fifi");
        d.getName().equals("Fifi"); // true
        System.out.println("mem address of FIFI: " + d);
    }

    // public static void main(String[] args) {
    //     Dog aDog = new Dog("Max");
    //     Dog oldDog = aDog;
    
    //     foo(aDog);
    //     // when foo(...) returns, the name of the dog has been changed to "Fifi"
    //     aDog.getName().equals("Fifi"); // true
    //     // but it is still the same dog:
    //     System.out.println(aDog == oldDog); // true
    // }
    
    // public static void foo(Dog d) {
    //     d.getName().equals("Max"); // true
    //     // this changes the name of d to be "Fifi"
    //     d.setName("Fifi");
    // }
}

class Dog {
    private String name;
    public Dog(String s) {
        name = s;
    }

    public void setName(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}