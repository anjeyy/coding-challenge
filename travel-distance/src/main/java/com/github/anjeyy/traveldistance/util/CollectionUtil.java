package com.github.anjeyy.traveldistance.util;

import java.util.Collection;
import java.util.List;

/**
 * Personal collection utilization for convenient methods.
 *
 * @author Andjelko Perisic
 */
public final class CollectionUtil {

  private CollectionUtil() {
    throw new UnsupportedOperationException(
      String.format("No instance of '%s' allowed.", getClass())
    );
  }

  /**
   * A convenient method negating the {@link Collection#isEmpty()} method.
   *
   * @param collection any collection
   * @param <E> erasure type
   * @return true if NOT empty, false otherwise
   */
  public static <E> boolean isNotEmpty(Collection<E> collection) {
    return !collection.isEmpty();
  }

  /**
   * Extract the last element for any given {@link List}.
   *
   * @param list any list
   * @param <E> erasure type
   * @return last element
   * @throws IllegalArgumentException for any <code>null</code> or <i>empty list</i>
   */
  public static <E> E retrieveLastElement(List<E> list) {
    if (list == null || list.isEmpty()) {
      throw new NullPointerException("Provide a non-null and non-empty list.");
    }
    int lastIndex = list.size() - 1;
    return list.get(lastIndex);
  }
}
