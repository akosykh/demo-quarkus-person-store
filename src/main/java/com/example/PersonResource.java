package com.example;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

@Path("person")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @POST
    public Uni<Response> create(Person person) {
        if (person == null || person.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return Panache.withTransaction(person::persist)
                .replaceWith(Response.ok(person).status(CREATED)::build);
    }

    @GET
    @Path("{id}")
    public Uni<PanacheEntityBase> personById(@PathParam("id") String id) {
        return Person.findById(id);
    }

    @GET
    public Uni<List<Person>> get() {
        return Person.listAll(Sort.by("lastname"));
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam("id") Long id, Person person) {
        if (person == null || person.firstname == null || person.lastname == null) {
            throw new WebApplicationException("Person firstname or lastname was not set on request.", 422);
        }

        return Panache
                .withTransaction(() -> Person.<Person> findById(id)
                        .onItem().ifNotNull().invoke(entity -> {
                            entity.lastname = person.lastname;
                            entity.firstname = person.firstname;
                            if (person.birthday != null) {
                                entity.birthday = person.birthday;
                            }
                        })
                )
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Panache.withTransaction(() -> Person.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(NO_CONTENT).build()
                        : Response.ok().status(NOT_FOUND).build());
    }


}