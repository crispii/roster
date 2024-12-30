package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked (singly linked-list) implementation of IndexedList.
 *
 * @param <T> Element type.
 */
public class LinkedIndexedList<T> implements IndexedList<T> {

  private Node<T> head;
  private final int length;

  /**
   * Constructs a new LinkedIndexedList of length size
   * with default value of defaultValue.
   *
   * @param size Length of list, expected: size > 0.
   * @param defaultValue Default value to store in each slot.
   * @throws LengthException if size <= 0.
   */
  public LinkedIndexedList(int size, T defaultValue) throws LengthException {
    if (size <= 0) {
      throw new LengthException();
    }

    length = size;

    // Unlike in ArrayIndexedList we cannot avoid the initialization even
    // if defaultValue == null since the nodes still have to be created.
    for (int i = 0; i < length; i++) {
      prepend(defaultValue);
    }
  }

  // Insert a node at the beginning of the linked list.
  private void prepend(T t) {
    Node<T> n = new Node<T>();
    n.data = t;
    n.next = head;
    head = n;
  }

  private boolean isValid(int index) {
    return index >= 0 && index < length();
  }

  // Find the node for a given index.
  private Node<T> find(int index) throws IndexException {
    if (!isValid(index)) {
      throw new IndexException();
    }

    Node<T> node = head;
    int counter = 0;
    while (node != null && counter < index) {
      node = node.next;
      counter = counter + 1;
    }
    return node;
  }

  @Override
  public T get(int index) throws IndexException {
    Node<T> node = find(index);
    return node.data;
  }

  @Override
  public void put(int index, T value) throws IndexException {
    Node<T> node = find(index);
    node.data = value;
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public Iterator<T> iterator() {
    return new LinkedIndexListIterator();
  }

  // Node does not have access to members of LinkedIndexedList
  // because it is static
  private static class Node<T> {
    T data;
    Node<T> next;
  }

  // An iterator to traverse the linked list from front (head) to back.
  private class LinkedIndexListIterator implements Iterator<T> {
    private Node<T> current;

    LinkedIndexListIterator() {
      current = head;
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      T t = current.data;
      current = current.next;
      return t;
    }
  }
}
