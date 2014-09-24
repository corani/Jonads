package nl.loadingdata.jonads.monads;

import nl.loadingdata.jonads.Appliable;
import nl.loadingdata.jonads.Functor;
import nl.loadingdata.jonads.JonadException;
import nl.loadingdata.jonads.Monad;

@SuppressWarnings("rawtypes")
public class Identity<A> extends Monad<Identity, A> {
	private A val;

	public Identity(A val) {
		this.val = val;
	}
	
	@Override
	public <U> Appliable<? extends Functor<Identity, A>,
						 ? extends Functor<Identity, U>> fmap(Appliable<A, U> f) throws JonadException {
		return (Identity<A> arg) -> cast(arg);
	}

	@Override
	public Functor<Identity, ?> join() throws JonadException {
		return this;
	}
	
	@Override
	public A val() throws JonadException {
		return val;
	}
	
	@SuppressWarnings("unchecked")
	private <U> Identity<U> cast(Identity<A> arg) {
		return (Identity<U>) arg;
	}
}
