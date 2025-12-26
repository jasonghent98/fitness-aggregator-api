package com.jasonghent98.fitness_aggregator_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;


@Slf4j
@Service
public class EmailService {

    private final SesClient sesClient;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    public EmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    /** Sends asynchronous emails related to app-related events (i.e. billing, authentication, etc.) */
    @Async
    public void sendEmail(String to, String subject, String bodyHtml) {
        try {
            Destination destination = Destination.builder()
                    .toAddresses(to)
                    .build();

            Content subjectContent = Content.builder()
                    .data(subject)
                    .charset("UTF-8")
                    .build();


            Content htmlContent = Content.builder()
                    .data(bodyHtml)
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .html(htmlContent)
                    .build();

            Message message = Message.builder()
                    .subject(subjectContent)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(destination)
                    .message(message)
                    .build();

            sesClient.sendEmail(request);
            log.info("Email successfully sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
}