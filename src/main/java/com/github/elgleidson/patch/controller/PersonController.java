package com.github.elgleidson.patch.controller;

import com.github.elgleidson.patch.controller.converter.DomainConverter;
import com.github.elgleidson.patch.controller.converter.DomainMerger;
import com.github.elgleidson.patch.controller.domain.PersonRequest;
import com.github.elgleidson.patch.controller.domain.PersonResponse;
import com.github.elgleidson.patch.service.PersonService;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/people")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PersonController {

  private final PersonService service;
  private final DomainConverter domainConverter;
  private final DomainMerger domainMerger;

  @GetMapping
  public Mono<ResponseEntity<List<PersonResponse>>> getAll() {
    return service.getAll()
      .map(domainConverter::convertToResponse)
      .collectList()
      .map(ResponseEntity::ok)
      .defaultIfEmpty(ResponseEntity.notFound().build())
      .doFirst(() -> log.info("enter getAll()"))
      .doOnSuccess(response -> log.info("exit getAll(): response={}", response));
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<PersonResponse>> get(@PathVariable String id) {
    return service.get(id)
      .map(domainConverter::convertToResponse)
      .map(ResponseEntity::ok)
      .defaultIfEmpty(ResponseEntity.notFound().build())
      .doFirst(() -> log.info("enter get(): id={}", id))
      .doOnSuccess(response -> log.info("exit get(): response={}", response));
  }

  @PostMapping
  public Mono<ResponseEntity<String>> create(@Valid @RequestBody PersonRequest request) {
    return Mono.just(request)
      .map(domainConverter::convertFromRequest)
      .flatMap(service::create)
      .map(id -> {
        var uri = UriComponentsBuilder.fromPath("/people/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(id);
      })
      .doFirst(() -> log.info("enter create(): request={}", request))
      .doOnSuccess(response -> log.info("exit create(): response={}", response));
  }


  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  public Mono<ResponseEntity<PersonResponse>> patch(@PathVariable String id,
                                                    @Schema(implementation = PersonRequest.class)
                                                    @RequestBody JsonMergePatch request) {
    return service.get(id)
      .map(person -> domainMerger.apply(person, request))
      .flatMap(person -> service.update(id, person))
      .map(domainConverter::convertToResponse)
      .map(ResponseEntity::ok)
      .defaultIfEmpty(ResponseEntity.notFound().build())
      .onErrorMap(BindException.class, bindException -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "", bindException))
      .doFirst(() -> log.info("enter patch(): id={}, request={}", id, request))
      .doOnSuccess(response -> log.info("exit patch(): response={}", response));
  }

}
