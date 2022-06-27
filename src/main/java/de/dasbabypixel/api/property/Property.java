package de.dasbabypixel.api.property;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author DasBabyPixel
 * @param <T>
 */
public interface Property<T> {

	/**
	 * Adds a {@link ChangeListener}
	 * 
	 * @param listener
	 */
	void addListener(final ChangeListener<? super T> listener);

	/**
	 * Removes a {@link ChangeListener}
	 * 
	 * @param listener
	 */
	void removeListener(final ChangeListener<? super T> listener);

	/**
	 * Adds an {@link InvalidationListener}
	 * 
	 * @param listener
	 */
	void addListener(final InvalidationListener listener);

	/**
	 * Removes an {@link InvalidationListener}
	 * 
	 * @param listener
	 */
	void removeListener(final InvalidationListener listener);

	/**
	 * @return the {@link InvalidationListener}s
	 */
	Collection<InvalidationListener> getInvalidationListeners();

	/**
	 * @return the {@link ChangeListener}s
	 */
	Collection<ChangeListener<? super T>> getChangeListeners();

	/**
	 * Adds one or more dependencies to this property. When the dependency is
	 * invalidated, this property is invalidated
	 * 
	 * @param dependencies
	 * @return this
	 */
	Property<T> addDependencies(final Property<?>... dependencies);

	/**
	 * Removes one or more dependencies
	 * 
	 * @param dependencies
	 * @return this
	 */
	Property<T> removeDependencies(final Property<?>... dependencies);

	/**
	 * Disposes this property. Run cleanup code here
	 */
	void dispose();

	/**
	 * @return the value of this property
	 */
	T getValue();

	/**
	 * Sets the value of this property
	 * 
	 * @param value
	 */
	void setValue(final T value);

	/**
	 * @param value
	 * @return if the value of this property equals the given object
	 */
	boolean hasValue(final T value);

	/**
	 * @return if this property is valid
	 */
	boolean isValid();

	/**
	 * Invalidates this property. Called when value is changed
	 */
	void invalidate();

	/**
	 * Binds this property to another The value of this property will always be the
	 * value of the other property
	 * 
	 * @param other
	 */
	void bind(final Property<T> other);

	/**
	 * Unbinds this property
	 */
	void unbind();

	/**
	 * Binds this property bidirectionally to another. Changing the value of one
	 * property will change the value of the other
	 * 
	 * @param other
	 */
	void bindBidirectional(final Property<T> other);

	/**
	 * @return if this property is bound
	 */
	boolean isBound();

	/**
	 * @return if this property is bound bidirectional
	 */
	boolean isBoundBidirectional();

	/**
	 * @return the property this property is bound to
	 */
	Property<T> getBoundTo();

	/**
	 * @return the storage of this property
	 */
	@Deprecated
	Storage<T> getStorage();

	/**
	 * @return the {@link BindingRedirectListener} of this property
	 */
	@Deprecated
	BindingRedirectListener<T> getBindingRedirectListener();

	/**
	 * Maps this property with the given function
	 * 
	 * @param <V>
	 * @param function
	 * @return the new property
	 */
	<V> Property<V> map(final Function<T, V> function);

	/**
	 * Maps this property to a {@link BooleanValue}
	 * 
	 * @return the new property
	 */
	BooleanValue mapToBoolean();

	/**
	 * Maps this property to a {@link BooleanValue} with the given function
	 * 
	 * @param function
	 * @return the new property
	 */
	BooleanValue mapToBoolean(final BooleanMapFunction<T> function);

	/**
	 * Maps this property to a {@link NumberValue}
	 * 
	 * @return the new property
	 */
	NumberValue mapToNumber();

	/**
	 * Maps this property to a {@link NumberValue} with the given function
	 * 
	 * @param function
	 * @return the new property
	 */
	NumberValue mapToNumber(final NumberMapFunction<T> function);
}
