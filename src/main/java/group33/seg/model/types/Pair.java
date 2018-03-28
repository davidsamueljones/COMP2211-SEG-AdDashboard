package group33.seg.model.types;

public class Pair<K, V> {
  public K key;
  public V value;

  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public int hashCode() {
    return key.hashCode() + value.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Pair<?,?>) {
      Pair<?,?> otherPair = (Pair<?,?>) other;
      return this.key.equals(otherPair.key) & this.value.equals(otherPair.value);
    } else {
      return false;
    }
  }
}
