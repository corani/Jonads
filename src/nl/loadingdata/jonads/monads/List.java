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
				return (List<J>) this;
			} catch (ClassCastException e) {
				throw new JonadException("Not a List");
			}
		}

		@Override
		public J val() throws JonadException {
			return head;
		}
		
		@Override
		public String toString() {
			return "Cons<" + head + ", " + tail + ">";
		}
	}

	public static <T> List<T> cons(T head, List<T> tail) {
		return new Cons<T>(head, tail);
	}
	
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
				U apply = f.apply(c.head);
				while (apply != List.nil()) {
					@SuppressWarnings("unchecked")
					Cons<A> cc = (Cons<A>) apply;
					result = List.cons(cast(cc.head), result);
					apply = cast(cc.tail);
				}
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

	@SuppressWarnings("unchecked")
	private <T> T cast(A head) {
		return (T) head;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T cast(List<A> tail) {
		return (T) tail;
	}

}
