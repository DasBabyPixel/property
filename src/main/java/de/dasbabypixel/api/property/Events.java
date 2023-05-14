package de.dasbabypixel.api.property;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

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
        if (property.boundTo != null && property.boundTo.boundTo == property) {
            property.boundTo.invalidate();
        }
        size = property.dependants.size();
        final int initialSize = size;
        boolean rem = false;
        for (int i = 0; ; i++) {
            if (i >= size)
                break;
            try {
                WeakReference<Property<?>> ref = property.dependants.get(i);
                Property<?> property = ref.get();
                if (property == null) {
                    this.property.dependants.remove(i--);
                    rem = true;
                    size--;
                    continue;
                }
                property.invalidate();
                if (size != this.property.dependants.size()) {
                    Events<?> e = ((AbstractProperty<?>) property).events;
                    String sb = size + "(" + initialSize + ")" + "->" + rem + this.property.dependants.size() + " | " + System.identityHashCode(this.property.dependants) + " " + property + '\n' + e;
                    throw new RuntimeException("Property removed a dependant during invalidation: " + sb);
                }
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException("Property removed a dependant during invalidation", ex);
            }
        }
    }

    public void change(T oldValue, T newValue) {
        int size = changeListeners.size();
        for (int i = 0; i < size; i++)
            changeListeners.get(i).handleChange(property, oldValue, newValue);
    }

    @Override
    public String toString() {
        return "Events{" + "invalidationListeners=" + invalidationListeners + ", changeListeners=" + changeListeners + '}';
    }
}
