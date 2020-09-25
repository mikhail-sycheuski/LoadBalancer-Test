package com.iptiQ.test.loadbalancer.id;

import java.util.UUID;

public class UniqueStringIdGeneratorImplUUID implements UniqueStringIdGenerator {

  /**
   * Generates UUID v.4 based string id with high order of uniqueness. This is not highly performant
   * algorithm implementation however due to the restrictions in library usage and testing purpose
   * it should suite the needs.
   *
   * @return - string representation of the UUID v.4
   */
  @Override
  public String generateNextId() {
    return UUID.randomUUID().toString();
  }
}
