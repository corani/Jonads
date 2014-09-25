package nl.loadingdata.jonads.example;

import java.util.function.BiFunction;

import nl.loadingdata.jonads.JonadException;
import nl.loadingdata.jonads.monads.List;
import nl.loadingdata.jonads.monads.Maybe;

public class Example {
	
	public static void main(String[] args) throws JonadException {
		List<Property<Integer>> props = List.of(
			new Property<>(2),
			new Property<>(3),
			new Property<>()
		);

		System.out.println ("Properties:");
		props.bind(Example::printList);
		
		List<Maybe<Integer>> maybes = List.map(props, (Property<Integer> p) -> {
			try {
				return p.get();
			} catch (JonadException e) {
				return Maybe.nothing();
			}
		});
		
		BiFunction<List<Maybe<Integer>>, List<Maybe<Integer>>, List<Maybe<Integer>>> listMult
			= List.lift((Maybe<Integer> m1, Maybe<Integer> m2) ->
				Maybe.lift((Integer i1, Integer i2) ->
					i1 * i2
			).apply(m1, m2)
		);
		
		List<Maybe<Integer>> result = listMult.apply(maybes, maybes);
		
		System.out.println("In-Product:");
		result.bind(Example::printList);
	}
	
	public static <T, U> List<T> printList(U val) {
		System.out.println(val.toString());
		return List.nil();
	}

}
