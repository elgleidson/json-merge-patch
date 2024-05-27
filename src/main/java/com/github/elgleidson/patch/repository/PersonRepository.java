package com.github.elgleidson.patch.repository;

import com.github.elgleidson.patch.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {

  Flux<Person> getAll();

  Mono<Person> get(String id);

  Mono<String> create(Person person);

  Mono<Person> update(String id, Person person);

}
