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

		System.out.println ("Properties:");
		System.out.println (p1.get());
		System.out.println (p2.get());
		System.out.println (p3.get());
		
		List<Maybe<Integer>> props = List.of(p1.get(), p2.get(), p3.get()); 
		
		BiFunction<List<Maybe<Integer>>, List<Maybe<Integer>>, List<Maybe<Integer>>> listMult
			= List.lift((Maybe<Integer> m1, Maybe<Integer> m2) ->
				Maybe.lift((Integer i1, Integer i2) ->
					i1 * i2
			).apply(m1, m2)
		);
		
		List<Maybe<Integer>> result = listMult.apply(props, props);
		System.out.println("In-Product:");
		result.bind(i -> {
			System.out.println(i);
			return List.just(i);
		});
	}

}
