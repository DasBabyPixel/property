package de.dasbabypixel.api.property;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class Events<T> {
	public final List<InvalidationListener> invalidationListeners = new ArrayList<>();
	public final List<ChangeListener<? super T>> changeListeners = new ArrayList<>();
	protected final AbstractProperty<T> property;

	public Events(AbstractProperty<T> property) {
		this.property = property;
	}

	public void invalidate() {
		int size = invalidationListeners.size();
		for (int i = 0; i < size; i++) {
			invalidationListeners.get(i).invalidated(property);
		}
		if (property.boundTo != null) {
			property.boundTo.invalidate();
		}
		size = property.dependants.size();
		for (int i = 0; i < size; i++) {
			WeakReference<Property<?>> ref = property.dependants.get(i);
			Property<?> property = ref.get();
			if (property == null) {
				this.property.dependants.remove(i--);
				size--;
				continue;
			}
			property.invalidate();
		}
	}

	public void change(T oldValue, T newValue) {
		int size = changeListeners.size();
		for (int i = 0; i < size; i++)
			changeListeners.get(i).handleChange(property, oldValue, newValue);
	}
}
