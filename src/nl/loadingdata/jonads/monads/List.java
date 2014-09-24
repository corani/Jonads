package nl.loadingdata.jonads.monads;

import java.util.function.BiFunction;

import nl.loadingdata.jonads.Appliable;
import nl.loadingdata.jonads.Functor;
import nl.loadingdata.jonads.JonadException;
import nl.loadingdata.jonads.Monad;

@SuppressWarnings("rawtypes")
public abstract class List<A> extends Monad<List, A> {
	public static final List<?> NIL = new List() {
		@Override
		public Functor<?, ?> join() throws JonadException {
			return this;
		}

		@Override
		public Object val() throws JonadException {
			throw new JonadException("Nil list has no value");
		}

		@Override
		protected List lsBind(Appliable arg) throws JonadException {
			return this;
		}
		
		public String toString() {
			return "Nil";
		};
	};
	
	public static class Cons<J> extends List<J> {
		private J head;
		private List<J> tail;

		public Cons(J head, List<J> tail) {
			this.head = head;
			this.tail = tail;
		}
		
		@Override
		public Functor<List, ?> join() throws JonadException {
			try {
				return (List<J>) tail;
			} catch (ClassCastException e) {
				throw new JonadException("tail is not a List");
			}
		}

		@Override
		public J val() throws JonadException {
			return head;
		}

		@Override @SuppressWarnings("unchecked")
		protected <T> T lsBind(Appliable<J, Monad<List, T>> f) throws JonadException {
			return (T) f.apply(head);
		}
		
		@Override
		public String toString() {
			return "Cons<" + head + ", " + tail + ">";
		}
	}

	public static <T> List<T> cons(T head, List<T> tail) {
		return new Cons<T>(head, tail);
	}
	
	protected abstract <T> T lsBind(Appliable<A, Monad<List, T>> f) throws JonadException;

	@SuppressWarnings("unchecked")
	public static <T> List<T> nil() {
		return (List<T>) NIL;
	}
	
	public static <T> List<T> just(T t) {
		return cons(t, nil());
	}
	
	@Override
	public <U> Appliable<? extends Functor<List, A>,
						 ? extends Functor<List, U>>
			fmap(Appliable<A, U> f) throws JonadException {
		return (List<A> arg) -> {
			List<U> result = List.nil();
			while (arg != List.nil()) {
				Cons<A> c = (Cons<A>) arg;
				result = List.cons((U) arg.lsBind(a -> List.just(f.apply(a))), result);
				arg = c.tail;
			}
			return result;
		};
	}
	
	public static <T, U, V> BiFunction<List<T>, List<U>, List<V>> lift(BiFunction<T, U, V> func) {
		return (List<T> l1, List<U> l2) -> {
			try {
				return (List<V>) l1.bind((T v1) ->
					l2.bind((U v2) ->
						List.just(func.apply(v1, v2))));
			} catch (JonadException e) {
				return List.nil();
			}
		};
	}

}
