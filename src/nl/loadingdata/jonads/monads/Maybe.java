package nl.loadingdata.jonads.monads;

import java.util.function.BiFunction;

import nl.loadingdata.jonads.Appliable;
import nl.loadingdata.jonads.Functor;
import nl.loadingdata.jonads.JonadException;
import nl.loadingdata.jonads.Monad;

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
	
	@Override
	public String toString() {
		String[] res = new String[1];
		try {
			bind((A x) -> {
				res[0] = x.toString();
				return just(x);
			});
		} catch (JonadException e) {
			e.printStackTrace();
		}
		if (res[0] == null) {
			res[0] = "NOTHING";
		}
		return "Maybe<" + res[0] + ">";
	}
	
	public static <T> Maybe<T> just(T x) {
		return new Just<T>(x);
	}

	@SuppressWarnings("unchecked")
	public static <T> Maybe<T> nothing() {
		return (Maybe<T>) NOTHING;
	}
	
	public static <T, U, V> BiFunction<Maybe<T>, Maybe<U>, Maybe<V>> lift(BiFunction<T, U, V> func) {
		return (Maybe<T> m1, Maybe<U> m2) -> {
			try {
				return (Maybe<V>) m1.bind((T v1) ->
					m2.bind((U v2) ->
						Maybe.just(func.apply(v1, v2))));
			} catch (Exception e) {
				return Maybe.nothing();
			}
		};
	}

}
