/**
 * The 4 main principles of OOP are:
 * 1. Abstraction - the use of objects that represent complexity. This means objects are black boxes and 
 *                  calling them will get us that result.
 * 2. Encapsulation - making certain fields hidden from the outside world. Fields are accessed and modified
 *                    only by the class itself. This allows for objects to be used system-wide as it can 
 *                    only be modified in one place.
 * 3. Inheritance - The ability to extend an object's functionality onto a child class.
 * 4. Polymorphism - The ability to see an object as a general type and the code itself can imply different
 *                   meanings. Method overloading and method overriding are examples of polymorphism.
 *                   For example ArrayList and LinkedList both have add() but the actual code itself may
 *                   be different but the functionality is the same.
 */

/**
 * Example below is of Abstract class, where it cannot be instantiated and must be extended.
 * Methods don't necessarily have to be implemented in this class. But final methods can be
 * and not changed in any subclass. Use an abstract class and inheritance if you can make the 
 * statement: A IS-A B.
 *
 * Note: since getSpeed is not an abstract method, it cannot be called if bike is instantiated
 * like: Bike b = new HondaBike(...); It must be HondaBike b = new HondaBike();
 *
 * https://www.javatpoint.com/abstract-class-in-java
 */
abstract class Bike {
	protected int tankSize;
	protected double mileage;
	Bike() {
		System.out.println("Bike is created!");
	}
	abstract void run();
	abstract double getMileage();
	abstract int getTankSize();
	void changeGear() {
		System.out.println("Changing gears!");
	}
}
class HondaBike extends Bike {
	HondaBike(int tankSize, double mileage) {
		this.tankSize = tankSize;
		this.mileage = mileage;
	}
	void run() {
		System.out.println("Honda bike running!");
	}
	double getMileage() {
		return mileage;
	}
	int getTankSize() {
		return tankSize;
	}
	String getSpeed() {
		return "vroom";
	}
}

/**
 * Example below is of an interface, also part of abstraction.
 * Can be used to achieve multiple inheritance in java and represents
 * an CAN-DO relationship. Use an interface if you can make the 
 * statement: A CAN-DO B. Is a blueprint of a class. 
 * Helps achieve loose coupling.
 * Interfaces can extend other interfaces.
 * https://www.javatpoint.com/interface-in-java
 */
interface Drawable {
	void draw();
}
interface Colorable {
	void color();
}
class Rectangle implements Drawable {
	public void draw() {
		System.out.println("Drawing rectangle...");
	}
}
class Circle implements Drawable,Colorable { // multiple inheritance
	public void color() {
		System.out.println("Coloring Circle");
	}
	public void draw() {
		System.out.println("Drawing Circle...");
	}
}

/**
 * Inheritance allows for polymorphism
 * https://www.javatpoint.com/inheritance-in-java
 */
class Animal {
	void eat() {
		System.out.println("Eating...");
	}
}
class Cat extends Animal {
	void talk() {
		System.out.println("Meowing...");
	}
}

/**
 * Example below is for polymorphism.
 * Polymorphism basically means to perform a single action in multiple ways.
 * Runtime polymorphism: a call to an overridden method is resolved at runtime over compile time.
 * This can be done thru upcasting. Animal car = new Cat() as method invocationis determined by JVM.
 * Example of this is above with abstract class.
 * https://www.javatpoint.com/runtime-polymorphism-in-java
 */
class Bank {
	protected int value;
	public Bank() {
		this.value = 0;
	}
	public Bank(int value) {
		this.value = value;
	}
	void announce() {
		System.out.println("This is a plain old bank");
	}
	void customer(int line) {
		System.out.println("This plain old bank has " + line + "customers!");
	}
}
class Citi extends Bank {
	private int savings;
	public Citi() {
		this.value = 0;
		this.savings = 0;
	}
	public Citi(int value) {
	       this.value = value;
       		this.savings = 0;
	}		
	public Citi(int value, int savings) {
		this.value = value;
		this.savings = savings;
	}
	// overriding - same exact signature
	void announce() {
		System.out.println("This is THE CitiBank!");
	}
	// overloading, adding/removing parameters
	void customer(int line) {
		System.out.println("CitiBank can serve " + line + " customers!");
	}
}

public class Main {
	public static void main(String... args) {
		System.out.println("Starting...");
		// Abstraction (includes encapsulation)
		Bike honda = new HondaBike(7, 25);
		honda.run();
		honda.changeGear();
		System.out.println("The mileage of this honda is: " + honda.getMileage() + " and the tank size is: " + honda.getTankSize());

		Rectangle r = new Rectangle();
		r.draw();
		Circle c = new Circle();
		c.draw();
		c.color();

		// Inheritance
		Animal animal = new Animal();
		animal.eat();
		Cat cat = new Cat();
		cat.eat();
		cat.talk();

		// Polymorphism
		Bank b = new Bank(5);
		b.announce();
		b.customer(10);
		Citi citi = new Citi(100, 90000);
		citi.announce();
		citi.customer(20);
	}
}
