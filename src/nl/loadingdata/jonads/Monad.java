package nl.loadingdata.jonads;

public abstract class Monad<M, A> implements Joinable<M, A> {
	@SuppressWarnings("unchecked")
	public <T> Monad<M, T> bind(Appliable<A, Monad<M, T>> f) throws JonadException {
		Appliable<Monad<M, A>, Monad<M, Monad<M, T>>> a = (Appliable<Monad<M, A>, Monad<M, Monad<M, T>>>) fmap(f);
		return (Monad<M, T>) a.apply(this).join();
	}
	
}
