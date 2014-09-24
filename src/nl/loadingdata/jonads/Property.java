package nl.loadingdata.jonads;

@SuppressWarnings("rawtypes")
public class Property<T> {
	private T val;
	
	public Property() {
	}
	
	public Property(T val) {
		this.val = val;
	}
	
	public void set(T val) {
		this.val = val;
	}
	
	public T get() {
		return val;
	}

	public Appliable<Maybe<T>, Monad<Maybe, Property<T>>> setter() {
		return v -> {
			return cast(v.bind((T i) -> {
				set(i);
				return Maybe.just(i);
			}));
		};
	}

	public Appliable<Property<T>, Maybe<T>> getter() {
		return (Property<T> p) -> {
			Maybe<T> ans = cast(Maybe.NOTHING);
			T res = get();
			if (res != null) {
				ans = Maybe.just(res);
			}
			return ans;
		};
	}
	
	@SuppressWarnings("unchecked")
	private Monad<Maybe, Property<T>> cast(Monad<Maybe, T> bind) {
		return (Monad<Maybe, Property<T>>) bind;
	}

	@SuppressWarnings("unchecked")
	private Maybe<T> cast(Maybe<?> nothing) {
		return (Maybe<T>) nothing;
	}
	
}
