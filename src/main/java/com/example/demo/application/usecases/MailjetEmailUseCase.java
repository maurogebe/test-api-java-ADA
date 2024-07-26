package com.example.demo.application.usecases;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailjetEmailUseCase {

//    private final MailjetClient client;

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    public void sendEmail(List<String> emailsTo, String subject, String body) throws MailjetException {
        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecret)
                .build();
        MailjetClient client = new MailjetClient(options);

        List<SendContact> emails = new ArrayList<>();
        for (String email : emailsTo) {
            emails.add(new SendContact(email, "stanislav"));
        }

        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(emails)
                .from(new SendContact("maurogebe.96@gmail.com", "Mailjet integration test"))
                .htmlPart(body)
                .subject(subject)
                .trackOpens(TrackOpens.ENABLED)
//                .attachment(Attachment.fromFile(attachmentPath))
                .header("test-header-key", "test-value")
                .customID("custom-id-value")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1)
                .build();

        request.sendWith(client);
    }
}
