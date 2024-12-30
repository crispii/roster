package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * An implementation of an IndexedList designed for cases where
 * only a few positions have distinct values from the initial value.
 *
 * @param <T> Element type.
 */
public class SparseIndexedList<T> implements IndexedList<T> {
  private Node<T> head;
  private final T defaultValue;
  private final int size;

  /**
   * Constructs a new SparseIndexedList of length size
   * with default value of defaultValue.
   *
   * @param size         Length of list, expected: size > 0.
   * @param defaultValue Default value to store in each slot.
   * @throws LengthException if size <= 0.
   */
  public SparseIndexedList(int size, T defaultValue) throws LengthException {
    if (size > 0) {
      this.defaultValue = defaultValue;
      this.size = size;
    } else {
      throw new LengthException("Given length is zero or negative.");
    }
  }

  @Override
  public int length() {
    return size;
  }

  @Override
  public T get(int index) throws IndexException {
    if (index < 0 || index >= length()) {
      throw new IndexException("get() received an invalid index.");
    }
    Node<T> node = head;
    while (node != null && node.index < index) {
      node = node.next;
    }
    if (node == null || node.index != index) {
      return defaultValue;
    } else {
      return node.data;
    }
  }

  @Override
  public void put(int index, T value) throws IndexException {
    if (index < 0 || index >= length()) {
      throw new IndexException("put() received an invalid index.");
    }
    if (head == null) { // New node becomes head.
      addHead(index, value);
    } else if (index < head.index) {
      insertHeadNode(index, value);
    } else {
      Node<T> current = head;
      Node<T> prev = null;
      while (current != null && current.index < index) { // traverse list.
        prev = current;
        current = current.next;
      }
      insertNonHeadNode(index, value, prev, current);
    }
  }

  /**
   * Deletes node at given index.
   * @param prev node from current.
   * @param current node in the list.
   * @param index   of new default node.
   */
  public void deleteNode(int index, Node<T> prev, Node<T> current) {
    if (head == null) {
      return;
    }
    if (head.index == index) {
      head = head.next;
      return;
    }
    if (current.next == null) {
      prev.next = null;
    } else {
      prev.next = current.next;
    }
  }

  /**
   * Insert new node at a non-head position.
   * It takes the new node's index and value, and inserts it into the list.
   *
   * @param index   of new node.
   * @param value   of new node.
   * @param prev    node of the current node.
   * @param current node in the list.
   */
  public void insertNonHeadNode(int index, T value, Node<T> prev, Node<T> current) {
    Node<T> newNode = new Node<>();
    newNode.data = value;
    newNode.index = index;
    if (current == null) { // reached right end of list
      if (value != null && !value.equals(defaultValue)) { // if unique, add the new node and if it's default, do nothing
        prev.next = newNode;
      } else {
        prev.next = newNode;
      }
    } else if (index != current.index) { // need to add in between prev and curr
      addInBetween(index, value, prev, current); // no need to check for if value is default (needing deletion)
    } else { // the index is already defined within the list
      if (value != null && value.equals(defaultValue)) { // value is default, so delete that node
        deleteNode(index, prev, current);
      } else { // value is unique, just update that node
        current.data = value;
      }
    }
  }

  /**
   * Insert new node in between previous and current.
   * It takes the new node's index and value, and inserts it in between prev and current.
   * @param index of new node.
   * @param value of new node.
   * @param prev node in the list.
   * @param current node in the list.
   */
  public void addInBetween(int index, T value, Node<T> prev, Node<T> current) {
    Node<T> newNode = new Node<>();
    newNode.data = value;
    newNode.index = index;
    if (!value.equals(defaultValue)) {
      newNode.next = current;
      prev.next = newNode;
    }
  }

  /**
   * Insert new node at head.
   * It takes the new node's index and value, and inserts it at the head of the list.
   * @param index of new node.
   * @param value of new node.
   */
  public void addHead(int index, T value) {
    Node<T> newNode = new Node<>();
    newNode.data = value;
    newNode.index = index;
    head = newNode;
  }

  /**
   * Insert node at head position.
   * It takes the new node's index and value, and inserts it at the head of the list.
   * @param index of new node.
   * @param value of new node.
   */
  public void insertHeadNode(int index, T value) {
    Node<T> newNode = new Node<>();
    newNode.data = value;
    newNode.index = index;
    newNode.next = head;
    head = newNode;
  }

  private static class Node<T> {
    T data;
    SparseIndexedList.Node<T> next;
    int index;
  }

  @Override
  public Iterator<T> iterator() {
    return new SparseIndexedListIterator();
  }

  private class SparseIndexedListIterator implements Iterator<T> {

    private SparseIndexedList.Node<T> current;
    private int index;

    SparseIndexedListIterator() {
      this.current = head;
      this.index = 0;
    }

    @Override
    public boolean hasNext() {
      return index < length();
    }

    @Override
    public T next() throws NoSuchElementException {
      T t;
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (current == null || current.index != index) { // if the index and current index are equal, is unique.
        t = defaultValue;
      } else {
        t = current.data;
        current = current.next;
      }

      while (current != null && current.index < index) { // move current to the next non-default value
        current = current.next;
      }
      index++;
      return t;
    }
  }
}