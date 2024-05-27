package com.github.com.elgleidson.patch.repository.domain;

import java.time.LocalDate;

public record PersonEntity(
  String id,
  String firstName,
  String lastName,
  LocalDate dateOfBirth,
  String email,
  String phoneNumber,
  String address,
  String city,
  String postCode
) {
}
