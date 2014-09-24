package nl.loadingdata.jonads;

@SuppressWarnings("rawtypes")
public abstract class Maybe<A> extends Monad<Maybe, A> {
	public static final Maybe<?> NOTHING = new Maybe() {
		@Override
		public Functor<?,?> join() throws JonadException {
			return this;
		}

		@Override
		public Object val() throws JonadException {
			throw new JonadException("Nothing has no value");
		}

		@Override
		protected Maybe<?> mbBind(Appliable arg) throws JonadException {
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
		public J val() throws JonadException {
			return value;
		}

		@Override
		protected <T> Maybe<T> mbBind(Appliable<J, Monad<Maybe, T>> f) throws JonadException {
			return (Maybe<T>) f.apply(value);
		}
	};

	protected abstract <T> Maybe<T> mbBind(Appliable<A, Monad<Maybe, T>> arg) throws JonadException;
	
	@Override
	public <T> Appliable<? extends Functor<Maybe, A>, ? extends Functor<Maybe, T>> map(Appliable<A, T> f) throws JonadException {
		return (Maybe<A> arg) -> arg.mbBind(a -> Maybe.just(f.apply(a)));
	}
	
	public static <T> Maybe<T> just(T x) {
		return new Just<T>(x);
	}
}
