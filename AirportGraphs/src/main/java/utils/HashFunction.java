package utils;

@FunctionalInterface
public interface HashFunction<K> {
  int function(K key);
  }
