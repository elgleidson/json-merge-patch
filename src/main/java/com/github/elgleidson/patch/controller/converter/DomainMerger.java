package com.github.elgleidson.patch.controller.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.elgleidson.patch.controller.domain.PersonRequest;
import com.github.elgleidson.patch.domain.Person;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

@Component
@Slf4j
@RequiredArgsConstructor
public class DomainMerger {

  private final ObjectMapper objectMapper;
  private final Validator validator;

  @SneakyThrows
  public Person apply(Person person, JsonMergePatch mergePatch) {
    log.debug("enter apply(): person={}, mergePatch={}", person, mergePatch);

    // convert the person back to what it would be the request to create that person.
    var personRequest = convertToRequest(person);
    // apply the merge patch
    var jsonNode = objectMapper.convertValue(personRequest, JsonNode.class);
    var patched = mergePatch.apply(jsonNode);
    // verify that the patched person request is a valid request
    var patchedRequest = objectMapper.treeToValue(patched, PersonRequest.class);
    validate(patchedRequest);

    var patchedPerson = convertFromRequest(person.id(), patchedRequest);
    log.debug("exit apply(): result={}", patchedPerson);
    return patchedPerson;
  }

  private PersonRequest convertToRequest(Person person) {
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

  private Person convertFromRequest(UUID id, PersonRequest request) {
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

  @SneakyThrows
  private void validate(PersonRequest patchedRequest) {
    var bindException = new BindException(PersonRequest.class, "request");
    validator.validate(patchedRequest, bindException);
    if (bindException.hasErrors()) {
      log.error("errors={}", bindException.getAllErrors());
      throw bindException;
    }
  }
}
