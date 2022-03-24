package com.bitpanda;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        log.debug(" starting http server");
        GreetingsHandler greetingsHandler = GreetingsHandler.create();
        var router = routes(greetingsHandler);
        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server -> {
                    startPromise.complete();
                    log.debug("HTTP server started on port " + server.actualPort());
                })
                .onFailure(event -> {
                    startPromise.fail(event);
                    log.debug("Failed to start HTTP server:" + event.getMessage());
                })
        ;

    }

    //create routes
    private Router routes(GreetingsHandler handlers) {

        // Create a Router
        Router router = Router.router(vertx);
        // register BodyHandler globally.
        //router.route().handler(BodyHandler.create());
        router.get("/greetings").produces("application/json").handler(handlers::all);
        router.post("/greetings").consumes("application/json").handler(handlers::save);
        router.get("/greetings/:id").produces("application/json").handler(handlers::get)
                .failureHandler(frc -> {
                    Throwable failure = frc.failure();
                    if (failure instanceof GreetingNotFoundException) {
                        frc.response().setStatusCode(404).end();
                    }
                    frc.response().setStatusCode(500).setStatusMessage("Server internal error:" + failure.getMessage()).end();
                });

        router.get("/hello").handler(rc -> rc.response().end("Hello from my route"));

        return router;
    }


}