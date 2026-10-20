
package com.educheck.assignment_service.client;

import com.educheck.assignment_service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class ClassroomClient {

    private final RestClient restClient;

    public ClassroomClient(
            @Value("${classroom.service.url}") String classroomServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(classroomServiceUrl)
                .build();
    }


    public void validateClassroom(Long classroomId) {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes)
                            RequestContextHolder.getRequestAttributes();

            String correlationId = null;

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                correlationId = request.getHeader("X-Correlation-ID");
            }

            var requestSpec = restClient.get()
                    .uri("/classrooms/{id}", classroomId);

            if (correlationId != null && !correlationId.isBlank()) {
                requestSpec.header("X-Correlation-ID", correlationId);
            }

            requestSpec.retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            (request, response) -> {
                                throw new ResourceNotFoundException(
                                        "Classroom not found with id: "
                                                + classroomId);
                            })
                    .toBodilessEntity();

        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException(
                        "Classroom not found with id: " + classroomId);
            }

            throw new IllegalStateException(
                    "Classroom Service returned HTTP "
                            + ex.getStatusCode().value(), ex);
        }
    }

}
