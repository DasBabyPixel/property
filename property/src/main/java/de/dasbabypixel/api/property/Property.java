package de.dasbabypixel.api.property;

import java.util.Collection;
import java.util.function.Function;

public interface Property<T> {

	void addListener(ChangeListener<? super T> listener);

	void removeListener(ChangeListener<? super T> listener);

	void addListener(InvalidationListener listener);

	void removeListener(InvalidationListener listener);

	Collection<InvalidationListener> getInvalidationListeners();

	Collection<ChangeListener<? super T>> getChangeListeners();
	
	Property<T> addDependencies(Property<?>... dependencies);
	
	Property<T> removeDependencies(Property<?>... dependencies);

	void dispose();

	T getValue();

	void setValue(T value);
	
	boolean hasValue(T value);

	boolean isValid();

	void invalidate();

	void bind(Property<T> other);

	void unbind();

	void bindBidirectional(Property<T> other);

	boolean isBound();
	
	boolean isBoundBidirectional();

	Property<T> getBoundTo();

	@Deprecated
	Storage<T> getStorage();
	
	@Deprecated
	BindingRedirectListener<T> getBindingRedirectListener();

	<V> Property<V> map(Function<T, V> function);

	BooleanValue mapToBoolean();

	BooleanValue mapToBoolean(BooleanMapFunction<T> function);

	NumberValue mapToNumber();

	NumberValue mapToNumber(NumberMapFunction<T> function);

}
