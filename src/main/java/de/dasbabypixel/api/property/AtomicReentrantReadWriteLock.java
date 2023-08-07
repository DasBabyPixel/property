package de.dasbabypixel.api.property;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class AtomicReentrantReadWriteLock implements ReadWriteLock {
	private final AtomicBoolean locking = new AtomicBoolean();
	private Thread owner;
	private int count = 0;
	private final Lock lock = new Lock() {
		@Override
		public void lock() {
			while (true) {
				if (!locking.compareAndSet(false, true))
					continue;
				if (owner == null)
					owner = Thread.currentThread();
				else if (owner != Thread.currentThread()) {
					locking.set(false);
					continue;
				}
				count++;
				locking.set(false);
				break;
			}
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean tryLock() {
			if (!locking.compareAndSet(false, true))
				return false;
			if (owner == null)
				owner = Thread.currentThread();
			else if (owner != Thread.currentThread()) {
				locking.set(false);
				return false;
			}
			count++;
			locking.set(false);
			return true;
		}

		@Override
		public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unlock() {
			while (true) {
				if (!locking.compareAndSet(false, true))
					continue;
				count--;
				if (count == 0)
					owner = null;
				locking.set(false);
				break;
			}
		}

		@NotNull
		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}
	};

	@NotNull
	@Override
	public Lock readLock() {
		return lock;
	}

	@NotNull
	@Override
	public Lock writeLock() {
		return lock;
	}
}
