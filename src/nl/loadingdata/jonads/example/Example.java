package nl.loadingdata.jonads.example;

import java.util.function.BiFunction;

import nl.loadingdata.jonads.JonadException;
import nl.loadingdata.jonads.monads.Maybe;

public class Example {
	
	public static void main(String[] args) throws JonadException {
		Property<Integer> p1 = new Property<>(2);
		Property<Integer> p2 = new Property<>(3);
		Property<Integer> p3 = new Property<>();

		System.out.println (p1.get());
		System.out.println (p2.get());
		System.out.println (p3.get());
		
		BiFunction<Maybe<Integer>, Maybe<Integer>, Maybe<Integer>> mult = Maybe.lift((Integer i1, Integer i2) -> i1 * i2);
		Property<Integer> pm = new Property<>(mult.apply(p1.get(), p2.get()));
		System.out.println(pm.get());
	}

}
