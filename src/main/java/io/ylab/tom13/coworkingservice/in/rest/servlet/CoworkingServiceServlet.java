package io.ylab.tom13.coworkingservice.in.rest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.tom13.coworkingservice.in.utils.Session;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.config.ApplicationConfig.getObjectMapper;

public abstract class CoworkingServiceServlet extends HttpServlet {

    protected final ObjectMapper objectMapper;

    protected final Session localSession = Session.getInstance();


    public CoworkingServiceServlet() {
        objectMapper = getObjectMapper();
    }

    protected String getJsonRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder jsonRequestBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonRequestBuilder.append(line);
        }
        return jsonRequestBuilder.toString();
    }

}
