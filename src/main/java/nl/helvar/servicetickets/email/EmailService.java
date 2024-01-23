package nl.helvar.servicetickets.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${SG_API_KEY}")
    private String apiKey;

    private Mail createMail(String recipient, String subject, String body) {
        Email from = new Email("serviceticketapp@gmail.com");
        Email to = new Email(recipient);
        Content content = new Content("text/plain", body);

        return new Mail(from, subject, to, content);
    }

    private void sendMail(Mail mail) throws IOException {
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }

    private String createTicketGreeting(String recipient) {
        return "Dear " + recipient + ",\n\n";
    }

    private String createTeamSignature() {
        return "\n\nThank you for choosing our services.\n\nBest regards,\nThe Helvar support team";
    }

    public void sendTicketConfirmationEmail(String recipient, Long id, String ticketName) throws IOException {
        String emailResponse = createTicketGreeting(recipient) +
                "We have received your service request for '" + ticketName + "' in good order.\n" +
                "This email is to confirm that your service ticket has been successfully created.\n" +
                "Your ticket number is #" + id + "\n" +
                "Our team will begin working on your request shortly." +
                createTeamSignature();

        Mail mail = createMail(recipient, "Confirmation of your service ticket", emailResponse);
        sendMail(mail);
    }

    public void sendTicketUpdate(String recipient, String ticketName, String responseBody) throws IOException {
        String emailResponse = createTicketGreeting(recipient) +
                "This email is to inform you there has been an update in your ticket '" + ticketName + "'.\n" +
                "the response reads as follows:\n\n" + responseBody +
                createTeamSignature();

        Mail mail = createMail(recipient, "Update of your service ticket", emailResponse);
        sendMail(mail);
    }

    public void sendUserCreationConfirmation(String recipient, String recipientFullName) throws IOException {
        String emailResponse = createTicketGreeting(recipientFullName) +
                "This email is to inform you that your account has been successfully created." +
                createTeamSignature();

        Mail mail = createMail(recipient, "Confirmation of your account creation", emailResponse);
        sendMail(mail);
    }
}
