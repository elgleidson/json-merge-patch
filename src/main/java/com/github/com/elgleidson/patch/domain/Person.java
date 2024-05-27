
package com.github.com.elgleidson.patch.domain;

import java.time.LocalDate;
import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;

public record Person(
  UUID id,
  PersonalDetails person,
  Address address,
  Contact contact
) {

  public record PersonalDetails(
    String firstName,
    String lastName,
    LocalDate dateOfBirth
  ) {
    public static final PersonalDetails NO_PERSONAL_DETAILS = new PersonalDetails(null, null, null);

    public boolean isPresent() {
      return ObjectUtils.anyNotNull(firstName, lastName, dateOfBirth);
    }
  }

  public record Address(
    String address,
    String city,
    String postCode
  ) {
    public static final Address NO_ADDRESS = new Address(null, null, null);

    public boolean isPresent() {
      return ObjectUtils.anyNotNull(address, city, postCode);
    }
  }

  public record Contact(
    String email,
    String phoneNumber
  ) {
    public static final Contact NO_CONTACT = new Contact(null, null);

    public boolean isPresent() {
      return ObjectUtils.anyNotNull(email, phoneNumber);
    }
  }

}
