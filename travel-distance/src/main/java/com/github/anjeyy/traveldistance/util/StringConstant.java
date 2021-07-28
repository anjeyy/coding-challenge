package com.github.anjeyy.traveldistance.util;

public enum StringConstant {
  NEW_LINE("\n"),
  EMPTY(""),
  WHITESPACE(" ");

  private final String value;

  StringConstant(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
