package com.bitpanda;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class GreetingsHandler {
    private List<Greeting> greetings = new ArrayList<>();

    public static GreetingsHandler create() {
        return new GreetingsHandler();
    }

    public void all(RoutingContext rc) {
        rc.response().end(Json.encode(greetings));
    }

    public void get(RoutingContext rc) {
        var params = rc.pathParams();
        var id = params.get("id");
        Optional<Greeting> greetingOptional =
                greetings.stream().filter(greeting -> greeting.id.equals(UUID.fromString(id))).findFirst();
        if (greetingOptional.isPresent()) {
            rc.response().end(Json.encode(greetingOptional.get()));
        } else {
            rc.fail(new Exception("no greeting found for id " + id));
        }
    }

    public void save(RoutingContext rc) {
        //rc.getBodyAsJson().mapTo(PostForm.class)
        var body = rc.getBodyAsJson();

        var form = body.mapTo(Greeting.class);
        greetings.add(form);
        rc.response()
                .putHeader("Location", "/posts/" + form.id)
                .setStatusCode(201)
                .end();

    }

}
