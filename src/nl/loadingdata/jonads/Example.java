package nl.loadingdata.jonads;

public class Example {
	
	public static void main(String[] args) throws JonadException {
		Property<Integer> p1 = new Property<>(2);
		Property<Integer> p2 = new Property<>(3);
		Property<Integer> p3 = new Property<>();

		print (p1.getter().apply());
		print (p2.getter().apply());
		print (p3.getter().apply());
		
		Property<Integer> pm = mult(p2, p1);
		print(pm.getter().apply());
	}

	private static Property<Integer> mult(Property<Integer> p1, Property<Integer> p2) {
		Property<Integer> pm = new Property<>();
		try {
			pm.setter().apply(mult(p1.getter().apply(), p2.getter().apply()));
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

	private static void print(Maybe<Integer> m) {
		try {
			m.bind((Integer i) -> {
				System.out.println(i);
				return m;
			});
		} catch (JonadException e) {
			e.printStackTrace();
		}
	}

}
