package nl.loadingdata.jonads;

public interface Joinable<F, T> extends Functor<F, T> {
	public Functor<F, ?> join() throws JonadException;
}
