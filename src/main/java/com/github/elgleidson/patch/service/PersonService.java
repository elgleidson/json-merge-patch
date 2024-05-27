package com.github.elgleidson.patch.service;

import com.github.elgleidson.patch.domain.Person;
import com.github.elgleidson.patch.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

  public final PersonRepository repository;

  public Flux<Person> getAll() {
    return repository.getAll();
  }

  public Mono<Person> get(String id) {
    return repository.get(id);
  }

  public Mono<String> create(Person person) {
    return repository.create(person);
  }

  public Mono<Person> update(String id, Person person) {
    return repository.update(id, person);
  }

}
