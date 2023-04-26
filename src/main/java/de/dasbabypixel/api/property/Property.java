package de.dasbabypixel.api.property;

import de.dasbabypixel.annotations.Api;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author DasBabyPixel
 */
@Api
public interface Property<T> {

	@Api
	static <T> Property<T> withValue(T value) {
		return new ObjectProperty<>(value);
	}

	@Api
	static <T> Property<T> withStorage(Storage<T> storage) {
		return new ObjectProperty<>(storage);
	}

	@Api
	static <T> Property<T> computing(Supplier<T> supplier) {
		return new ObjectProperty<>(new ComputerStorage<>(supplier));
	}

	static <T> Property<T> empty() {
		return withValue(null);
	}

	/**
	 * Adds a {@link ChangeListener}
	 */
	@Api
	void addListener(final ChangeListener<? super T> listener);

	/**
	 * Removes a {@link ChangeListener}
	 */
	@Api
	void removeListener(final ChangeListener<? super T> listener);

	/**
	 * Adds an {@link InvalidationListener}
	 */
	@Api
	void addListener(final InvalidationListener listener);

	/**
	 * Removes an {@link InvalidationListener}
	 */
	@Api
	void removeListener(final InvalidationListener listener);

	/**
	 * Adds one or more dependencies to this property. When the dependency is invalidated, this property is invalidated
	 *
	 * @return this
	 */
	@Api
	Property<T> addDependencies(final Property<?>... dependencies);

	/**
	 * Removes one or more dependencies
	 *
	 * @return this
	 */
	@Api
	Property<T> removeDependencies(final Property<?>... dependencies);

	/**
	 * Performs the given operation atomically.<br>
	 * <p>
	 * This property can not be modified from the outside during this operation.
	 *
	 * @param operation the operation to perform
	 */
	@Api
	void atomicOperation(Function<T, T> operation);

	/**
	 * @return all the properties depending on this property
	 */
	@Api
	Collection<Property<?>> dependants();

	/**
	 * @return the value of this property
	 */
	@Api
	T value();

	/**
	 * Sets the value of this property
	 */
	@Api
	void value(final T value);

	/**
	 * @return if the value of this property equals the given object
	 */
	@Api
	boolean hasValue(final T value);

	/**
	 * @return if this property is valid
	 */
	@Api
	boolean valid();

	/**
	 * Invalidates this property. Called when value is changed
	 */
	@Api
	void invalidate();

	/**
	 * Binds this property to another The value of this property will always be the value of the other property
	 */
	@Api
	void bind(final Property<T> other);

	/**
	 * Unbinds this property
	 */
	@Api
	void unbind();

	/**
	 * Binds this property bidirectionally to another. Changing the value of one property will change the value of the other
	 */
	@Api
	void bindBidirectional(final Property<T> other);

	/**
	 * @return if this property is bound
	 */
	@Api
	boolean bound();

	/**
	 * @return if this property is bound bidirectional
	 */
	@Api
	boolean boundBidirectional();

	/**
	 * @return the property this property is bound to
	 */
	@Api
	Property<T> boundTo();

	/**
	 * @return the storage of this property
	 */
	@Api
	Storage<T> storage();

	/**
	 * Maps this property with the given function
	 *
	 * @return the new property
	 */
	@Api
	<V> Property<V> map(final Function<T, V> function);

	/**
	 * Maps this property to a {@link BooleanValue}. This will only work if the underlying value is a boolean
	 *
	 * @return the new property
	 */
	@Api
	BooleanValue mapToBoolean();

	/**
	 * Maps this property to a {@link BooleanValue} with the given function
	 *
	 * @return the new property
	 */
	@Api
	BooleanValue mapToBoolean(final BooleanMapFunction<T> function);

	/**
	 * Maps this property to a {@link NumberValue}. This will only work if the underlying object is a number
	 *
	 * @return the new property
	 */
	@Api
	NumberValue mapToNumber();

	/**
	 * Maps this property to a {@link NumberValue} with the given function
	 *
	 * @return the new property
	 */
	@Api
	NumberValue mapToNumber(final NumberMapFunction<T> function);

	@Api
	NumberValue mapToDouble(final DoubleMapFunction<T> function);

	@Api
	NumberValue mapToFloat(final FloatMapFunction<T> function);

	@Api
	NumberValue mapToInteger(final IntegerMapFunction<T> function);

	@Api
	NumberValue mapToLong(final LongMapFunction<T> function);
}
