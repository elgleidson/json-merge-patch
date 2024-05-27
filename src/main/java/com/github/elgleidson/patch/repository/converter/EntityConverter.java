package com.github.elgleidson.patch.repository.converter;

import com.github.elgleidson.patch.domain.Person;
import com.github.elgleidson.patch.repository.domain.PersonEntity;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityConverter {

  public PersonEntity convertToEntity(Person person) {
    log.debug("enter convertToEntity(): personalDetails={}", person);

    var id = person.id() == null ? generateId() : person.id();
    var entity = new PersonEntity(id.toString(),
      person.person().firstName(), person.person().lastName(), person.person().dateOfBirth(),
      person.contact().email(), person.contact().phoneNumber(),
      person.address().address(), person.address().city(), person.address().postCode()
    );

    log.debug("exit convertToEntity(): result={}", entity);
    return entity;
  }

  public Person convertFromEntity(PersonEntity entity) {
    log.debug("enter convertFromEntity(): entity={}", entity);

    var person = new Person(UUID.fromString(entity.id()),
      new Person.PersonalDetails(entity.firstName(), entity.lastName(), entity.dateOfBirth()),
      new Person.Address(entity.address(), entity.city(), entity.postCode()),
      new Person.Contact(entity.email(), entity.phoneNumber())
    );

    log.debug("exit convertFromEntity(): result={}", person);
    return person;
  }

  private UUID generateId() {
    return UUID.randomUUID();
  }

}
