package group33.seg.lib;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {
  private static final long serialVersionUID = 6429490390263674160L;
  
  public final K key;
  public final V value;

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
      boolean keysMatch = false;
      boolean valuesMatch = false;

      if (this.key == null & otherPair.key == null){
        keysMatch = true;
      } else {
        if (this.key != null & otherPair.key != null) {
          keysMatch = this.key.equals(otherPair.key);
        }
      }

      if (this.value == null & otherPair.value == null){
        valuesMatch = true;
      } else {
        if (this.value != null & otherPair.value != null) {
          valuesMatch = this.value.equals(otherPair.value);
        }
      }

      return keysMatch & valuesMatch;
    } else {
      return false;
    }
  }
}
