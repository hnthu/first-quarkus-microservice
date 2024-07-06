package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/auth")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final Map<String, String> users = Map.of(
            "user1", "password1",
            "user2", "password2"
    );

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LoginResponse("Invalid request body"))
                    .build();
        }

        if (loginRequest.username() == null || loginRequest.password() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LoginResponse("Username and password are required"))
                    .build();
        }
        if (users.containsKey(loginRequest.username()) &&
                users.get(loginRequest.username()).equals(loginRequest.password())) {
            return Response.ok(new LoginResponse("Login successful")).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new LoginResponse("Invalid credentials"))
                    .build();
        }
    }
}

record LoginRequest(String username, String password) {}
record LoginResponse(String message) {}