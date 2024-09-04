package com.pubfinder.pubfinder.models;

public enum Volume {
  QUITE, PLEASANT, AVERAGE, LOUD, VERY_LOUD;

  public int getOrdinal() {
    return this.ordinal();
  }
}
