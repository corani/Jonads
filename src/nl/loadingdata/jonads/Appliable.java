package nl.loadingdata.jonads;

public interface Appliable<T, U> {
	public U apply(T t) throws JonadException;

	public default U apply() throws JonadException {
		return apply(null);
	}
}
