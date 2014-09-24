package nl.loadingdata.jonads;

@SuppressWarnings("rawtypes")
public class Property<T> {
	private T val;
	
	public Property() {
	}
	
	public Property(T val) {
		this.val = val;
	}
	
	public Property(Maybe<T> val) throws JonadException {
		set(val);
	}
	
	public Maybe<T> get() throws JonadException {
		return getter().apply();
	}
	
	public void set(Maybe<T> val) throws JonadException {
		setter().apply(val);
	}
	
	public Appliable<Maybe<T>, Monad<Maybe, Property<T>>> setter() {
		return (Maybe<T> v) ->
			cast(v.bind((T i) -> {
				val = i;
				return Maybe.just(i);
			}));
	}

	public Appliable<Property<T>, Maybe<T>> getter() {
		return (Property<T> p) -> {
			if (val == null) {
				return Maybe.nothing();
			} else {
				return Maybe.just(val);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private Monad<Maybe, Property<T>> cast(Monad<Maybe, T> bind) {
		return (Monad<Maybe, Property<T>>) bind;
	}

}
