package com.github.anjeyy.traveldistance.util;

import java.util.Collection;
import java.util.List;

public final class CollectionUtil {

  private CollectionUtil() {
    throw new UnsupportedOperationException(
      String.format("No instance of '%s' allowed.", getClass())
    );
  }

  public static <E> boolean isNotEmpty(Collection<E> collection) {
    return !collection.isEmpty();
  }

  public static <E> E retrieveLastElement(List<E> list) {
    if (list == null || list.isEmpty()) {
      throw new NullPointerException("Provide a non-null and non-empty list.");
    }
    int lastIndex = list.size() - 1;
    return list.get(lastIndex);
  }
}
