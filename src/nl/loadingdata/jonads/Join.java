package nl.loadingdata.jonads;

public interface Join<F, T> extends Functor<F, T> {
	public Functor<F, ?> join() throws JonadException;
}
