package nl.loadingdata.jonads;

public abstract class Monad<M, A> implements Join<M, A> {
	@SuppressWarnings("unchecked")
	public <T> Monad<M, T> bind(Apply<A, Monad<M, T>> f) throws JonadException {
		Apply<Monad<M, A>, Monad<M, Monad<M, T>>> a = (Apply<Monad<M, A>, Monad<M, Monad<M, T>>>) fmap(f);
		Monad<M, Monad<M, T>> mmonad = a.apply(this);
		return (Monad<M, T>) mmonad.join();
	}

	public Monad<M, A> fail(String s) throws JonadException {
		throw new JonadException(s);
	}
}
