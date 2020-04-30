package basics;

public interface Stack<T> extends Container<T> {
  void push(T item);
  T pop();
  T peek();
  }
