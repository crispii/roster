package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array implementation of IndexedList.
 * It takes a length, and a default value to "plaster" over the entire list.
 *
 * @param <T> Element type.
 */
public class ArrayIndexedList<T> implements IndexedList<T> {

  private final T[] data;

  /**
   * Constructs a new ArrayIndexedList of length size
   * with default value of defaultValue.
   *
   * @param size Length of array, expected: size > 0.
   * @param defaultValue Default value to store in each slot.
   * @throws LengthException if size <= 0.
   */
  public ArrayIndexedList(int size, T defaultValue) throws LengthException {
    if (size <= 0) {
      throw new LengthException();
    }

    // This cast results in warning but succeeds in creating a generic array.
    data = (T[]) new Object[size];
    // The resulting warning is acceptable because there simply is
    // no better way of doing this.

    // Array slots are null by default.
    if (defaultValue == null) {
      return;
    }

    for (int i = 0; i < size; i++) {
      data[i] = defaultValue;
    }
  }

  private boolean isValid(int index) {
    return index >= 0 && index < length();
  }

  @Override
  public T get(int index) throws IndexException {
    if (!isValid(index)) {
      throw new IndexException();
    }

    return data[index];
  }

  @Override
  public void put(int index, T value) throws IndexException {
    if (!isValid(index)) {
      throw new IndexException();
    }

    data[index] = value;
  }

  @Override
  public int length() {
    return data.length;
  }

  @Override
  public Iterator<T> iterator() {
    return new ArrayIndexedListIterator();
  }

  private class ArrayIndexedListIterator implements Iterator<T> {
    private int nextIndex;

    private ArrayIndexedListIterator() {
      nextIndex = 0;
    }

    @Override
    public boolean hasNext() {
      // you can directly access the private member 'data'
      return nextIndex < data.length;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      T t = data[nextIndex];
      nextIndex += 1;
      return t;
    }
  }
}
