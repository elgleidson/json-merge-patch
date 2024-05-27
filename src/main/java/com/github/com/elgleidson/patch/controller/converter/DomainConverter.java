package com.github.com.elgleidson.patch.controller.converter;

import com.github.com.elgleidson.patch.controller.domain.PersonRequest;
import com.github.com.elgleidson.patch.controller.domain.PersonResponse;
import com.github.com.elgleidson.patch.domain.Person;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainConverter {

  public Person convertFromRequest(PersonRequest request) {
    log.debug("enter convertFromRequest(): request={}", request);
    var person = convertFromRequest(null, request);
    log.debug("exit convertFromRequest(): result={}", person);
    return person;
  }

  public Person convertFromRequest(UUID id, PersonRequest request) {
    log.debug("enter convertFromRequest(): id={}, request={}", id, request);

    var personalDetails = Optional.ofNullable(request.personalDetails())
      .map(vm -> new Person.PersonalDetails(vm.firstName(), vm.lastName(), vm.dateOfBirth()))
      .orElse(Person.PersonalDetails.NO_PERSONAL_DETAILS);

    var address = Optional.ofNullable(request.address())
      .map(vm -> new Person.Address(vm.address(), vm.city(), vm.postCode()))
      .orElse(Person.Address.NO_ADDRESS);

    var contact = Optional.ofNullable(request.contact())
      .map(vm -> new Person.Contact(vm.email(), vm.phoneNumber()))
      .orElse(Person.Contact.NO_CONTACT);

    var person = new Person(id, personalDetails, address, contact);
    log.debug("exit convertFromRequest(): result={}", person);
    return person;
  }

  public PersonResponse convertToResponse(Person person) {
    log.debug("enter convertToResponse(): personalDetails={}", person);

    var personResponse = Optional.ofNullable(person.person())
      .filter(Person.PersonalDetails::isPresent)
      .map(personalDetails -> new PersonResponse.PersonalDetails(personalDetails.firstName(), personalDetails.lastName(), personalDetails.dateOfBirth()))
      .orElse(null);

    var addressResponse = Optional.ofNullable(person.address())
      .filter(Person.Address::isPresent)
      .map(address -> new PersonResponse.AddressDetails(address.address(), address.city(), address.postCode()))
      .orElse(null);

    var contactResponse = Optional.ofNullable(person.contact())
      .filter(Person.Contact::isPresent)
      .map(contact -> new PersonResponse.ContactDetails(contact.email(), contact.phoneNumber()))
      .orElse(null);

    var response = new PersonResponse(person.id().toString(), personResponse, addressResponse, contactResponse);
    log.debug("exit convertToResponse(): result={}", response);
    return response;
  }

  public PersonRequest convertToRequest(Person person) {
    log.debug("enter convertToRequest(): personalDetails={}", person);

    var personRequest = Optional.ofNullable(person.person())
      .filter(Person.PersonalDetails::isPresent)
      .map(personalDetails -> new PersonRequest.PersonalDetails(personalDetails.firstName(), personalDetails.lastName(), personalDetails.dateOfBirth()))
      .orElse(null);

    var addressRequest = Optional.ofNullable(person.address())
      .filter(Person.Address::isPresent)
      .map(address -> new PersonRequest.AddressDetails(address.address(), address.city(), address.postCode()))
      .orElse(null);

    var contactRequest = Optional.ofNullable(person.contact())
      .filter(Person.Contact::isPresent)
      .map(contact -> new PersonRequest.ContactDetails(contact.email(), contact.phoneNumber()))
      .orElse(null);

    var response = new PersonRequest(personRequest, addressRequest, contactRequest);
    log.debug("exit convertToRequest(): result={}", response);
    return response;
  }
}
