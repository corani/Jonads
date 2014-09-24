package nl.loadingdata.jonads;

public class Example {
	
	public static void main(String[] args) throws JonadException {
		Property<Integer> p1 = new Property<>(2);
		Property<Integer> p2 = new Property<>(3);
		Property<Integer> p3 = new Property<>();

		System.out.println (p1.get());
		System.out.println (p2.get());
		System.out.println (p3.get());
		
		Property<Integer> pm = mult(p2, p1);
		System.out.println(pm.get());
	}

	private static Property<Integer> mult(Property<Integer> p1, Property<Integer> p2) {
		Property<Integer> pm = new Property<>();
		try {
			pm.set(mult(p1.get(), p2.get()));
		} catch (JonadException e) {
			e.printStackTrace();
		}
		return pm;
	}

	private static Maybe<Integer> mult(Maybe<Integer> m1, Maybe<Integer> m2) throws JonadException {
		return (Maybe<Integer>)
				m1.bind((Integer i1) -> 
					m2.bind((Integer i2) ->
						Maybe.just(i1 * i2)));
	}

}
