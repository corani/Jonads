package nl.loadingdata.jonads.example;

import java.util.function.BiFunction;

import nl.loadingdata.jonads.JonadException;
import nl.loadingdata.jonads.monads.List;
import nl.loadingdata.jonads.monads.Maybe;

public class Example {
	
	public static void main(String[] args) throws JonadException {
		Property<Integer> p1 = new Property<>(2);
		Property<Integer> p2 = new Property<>(3);
		Property<Integer> p3 = new Property<>();

		System.out.println (p1.get());
		System.out.println (p2.get());
		System.out.println (p3.get());
		
		BiFunction<Maybe<Integer>, Maybe<Integer>, Maybe<Integer>> mult = Maybe.lift((Integer i1, Integer i2) -> {
			System.out.println(i1 + "*" + i2);
			return i1 * i2;
		});
		Property<Integer> pm = new Property<>(mult.apply(p1.get(), p2.get()));
		System.out.println (pm.get());
		
		List<Integer> list = List.nil();
		list = List.cons(1, list);
		list = List.cons(2, list);

		System.out.println(list);
		list.bind(i -> {
			System.out.println(i);
			return List.just(i);
		});
		
		List<Maybe<Integer>> props = List.nil();
		props = List.cons(p1.get(), props);
		props = List.cons(p2.get(), props);
		props = List.cons(p3.get(), props);
		
		BiFunction<List<Maybe<Integer>>, List<Maybe<Integer>>, List<Maybe<Integer>>> listMult = List.lift((Maybe<Integer> i1, Maybe<Integer> i2) -> mult.apply(i1, i2));
		System.out.println(props);
		List<Maybe<Integer>> result = listMult.apply(props, props);
		System.out.println(result);
	}

}
