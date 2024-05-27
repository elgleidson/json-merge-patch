package com.github.elgleidson.patch.repository;

import com.github.elgleidson.patch.domain.Person;
import com.github.elgleidson.patch.repository.converter.EntityConverter;
import com.github.elgleidson.patch.repository.domain.PersonEntity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryPersonRepository implements PersonRepository {

  private final Map<String, PersonEntity> database = new ConcurrentHashMap<>();

  private final EntityConverter entityConverter;

  @Override
  public Flux<Person> getAll() {
    return Flux.fromIterable(database.values())
      .map(entityConverter::convertFromEntity)
      .doFirst(() -> log.info("enter getAll()"))
      .doOnComplete(() -> log.info("exit getAll()"));
  }

  @Override
  public Mono<Person> get(String id) {
    return Mono.just(id)
      .mapNotNull(database::get)
      .map(entityConverter::convertFromEntity)
      .doFirst(() -> log.info("enter get(): id={}", id))
      .doOnSuccess(response -> log.info("exit get(): response={}", response));
  }

  @Override
  public Mono<String> create(Person person) {
    return Mono.just(person)
      .map(entityConverter::convertToEntity)
      .map(entity -> {
        database.put(entity.id(), entity);
        return entity.id();
      })
      .doFirst(() -> log.info("enter create(): person={}", person))
      .doOnSuccess(response -> log.info("exit create(): response={}", response));
  }

  @Override
  public Mono<Person> update(String id, Person person) {
    return Mono.just(person)
      .map(entityConverter::convertToEntity)
      .map(entity -> database.replace(id, entity))
      .map(oldEntity -> person)
      .doFirst(() -> log.info("enter update(): id={}, person={}", id, person))
      .doOnSuccess(response -> log.info("exit update(): response={}", response));
  }
}
