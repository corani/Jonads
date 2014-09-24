package nl.loadingdata.jonads;

public abstract class Maybe<A> extends Monad<Maybe, A> {
	public static final Maybe<?> NOTHING = new Maybe() {
		@Override
		public Functor<?,?> join() throws JonadException {
			return this;
		}

		@Override
		public Object arg() throws JonadException {
			throw new JonadException("Nothing has no value");
		}

		@Override
		protected Maybe<?> mbBind(Apply arg) throws JonadException {
			return this;
		}
	};
	
	public static final class Just<J> extends Maybe<J> {
		private J value;

		public Just(J value) {
			this.value = value;
		}
		
		@Override
		public Maybe<?> join() throws JonadException {
			try {
				return (Maybe<?>) value;
			} catch (ClassCastException e) {
				throw new JonadException("Can't join");
			}
		}

		@Override
		public J arg() throws JonadException {
			return value;
		}

		@Override
		protected <T> Maybe<T> mbBind(Apply<J, Monad<Maybe, T>> f) throws JonadException {
			return (Maybe<T>) f.apply(value);
		}
	};

	protected abstract <T> Maybe<T> mbBind(Apply<A, Monad<Maybe, T>> arg) throws JonadException;
	
	@Override
	public <T> Apply<? extends Functor<Maybe, A>, ? extends Functor<Maybe, T>> fmap(Apply<A, T> f) throws JonadException {
		return (Maybe<A> arg) -> arg.mbBind(a -> Maybe.pure(f.apply(a)));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Maybe<A> fail(String s) throws JonadException {
		return (Maybe<A>) NOTHING;
	}
	
	public static <T> Maybe<T> pure(T x) {
		return new Just<T>(x);
	}
}
