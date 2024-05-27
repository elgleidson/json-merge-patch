package com.github.elgleidson.patch.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record PersonRequest(
  @Valid
  PersonalDetails personalDetails,
  @Valid
  AddressDetails address,
  @Valid
  ContactDetails contact
) {

  public record PersonalDetails(
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @Past
    LocalDate dateOfBirth
  ) {
  }

  public record AddressDetails(
    @NotBlank
    String address,
    @NotBlank
    String city,
    @NotBlank
    String postCode
  ) {
  }

  public record ContactDetails(
    @Email
    String email,
    @Pattern(regexp = "\\d{1,20}")
    String phoneNumber
  ) {
  }
}
