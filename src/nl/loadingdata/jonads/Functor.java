package nl.loadingdata.jonads;

public interface Functor<F, T> {
	public <U> Apply<? extends Functor<F, T>, ? extends Functor<F, U>> fmap(Apply<T, U> f) throws JonadException;
	public T arg() throws JonadException;
}
