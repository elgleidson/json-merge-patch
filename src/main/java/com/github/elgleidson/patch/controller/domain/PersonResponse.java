package com.github.elgleidson.patch.controller.domain;

import java.time.LocalDate;

public record PersonResponse(
  String id,
  PersonalDetails personalDetails,
  AddressDetails address,
  ContactDetails contact
) {

  public record PersonalDetails(
    String firstName,
    String lastName,
    LocalDate dateOfBirth
  ) {
  }

  public record AddressDetails(
    String address,
    String city,
    String postCode
  ) {
  }

  public record ContactDetails(
    String email,
    String phoneNumber
  ) {
  }
}
