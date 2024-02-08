package nl.helvar.servicetickets.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${SG_API_KEY}")
    private String apiKey;

    private Mail createMail(String recipient, String subject, String header, String body) {
        Email from = new Email("serviceticketapp@gmail.com");
        Email to = new Email(recipient);
        Content content = new Content("text/html", body);

        Mail mail = new Mail(from, subject, to, content);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("mail_subject", subject);
        personalization.addDynamicTemplateData("user_greeting", header);
        personalization.addDynamicTemplateData("mail_body", body);


        mail.addPersonalization(personalization);
        mail.setTemplateId("d-230b81f32cc54c20a0d2953a8a227a78");

        return mail;
    }

    private void sendMail(Mail mail) throws IOException {
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw new IOException("Failed to send email: " + ex.getMessage());
        }
    }

    private String createTicketGreeting(String recipient) {
        return "Dear " + recipient + ",\n\n";
    }

    private String createTeamSignature() {
        return "<br><br>Thank you for choosing our services.<br><br>Best regards,<br>The Helvar support team";
    }

    public void sendTicketConfirmationEmail(String recipient, Long id, String ticketName) throws IOException {
        String header = createTicketGreeting(recipient);

        String body = "We have received your service request for '" + ticketName + "' in good order.<br>" +
                "This email is to confirm that your service ticket has been successfully created.<br>" +
                "Your ticket number is #" + id + "<br>" +
                "Our team will begin working on your request shortly." +
                createTeamSignature();

        Mail mail = createMail(recipient, "Confirmation of your service ticket", header, body);
        sendMail(mail);
    }

    public void sendTicketUpdate(String recipient, String ticketName, String responseBody) throws IOException {
        String header = createTicketGreeting(recipient);

        String body = "This email is to inform you there has been an update in your ticket '" + ticketName + "'.<br>" +
                "the response reads as follows:<br><br>" + responseBody +
                createTeamSignature();

        Mail mail = createMail(recipient, "Update of your service ticket", header, body);
        sendMail(mail);
    }

    public void sendUserCreationConfirmation(String recipient, String recipientFullName) throws IOException {
        String header = createTicketGreeting(recipientFullName);

        String body = "This email is to inform you that your account has been successfully created." +
                createTeamSignature();

        Mail mail = createMail(recipient, "Confirmation of your account creation", header, body);
        sendMail(mail);
    }
}
