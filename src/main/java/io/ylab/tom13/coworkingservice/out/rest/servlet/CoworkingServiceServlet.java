package io.ylab.tom13.coworkingservice.out.rest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import io.ylab.tom13.coworkingservice.out.utils.security.SecurityHTTPController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.config.ApplicationConfig.getObjectMapper;

public abstract class CoworkingServiceServlet extends SecurityHTTPController {

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

    protected void setJsonResponse(HttpServletResponse response, Object responseBody) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    protected void setJsonResponse(HttpServletResponse response, Object responseBody, int status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

}
