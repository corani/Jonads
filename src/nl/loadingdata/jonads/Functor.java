package nl.loadingdata.jonads;

public interface Functor<F, T> {
	public <U> Appliable<? extends Functor<F, T>,
						 ? extends Functor<F, U>>
			map(Appliable<T, U> f) throws JonadException;
	public T val() throws JonadException;
}
