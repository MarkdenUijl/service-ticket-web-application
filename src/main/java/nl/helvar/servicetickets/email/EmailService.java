package nl.helvar.servicetickets.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    private final Environment environment;

    @Value("${SG_API_KEY}")
    private String apiKey;

    public EmailService(Environment environment) {
        this.environment = environment;
    }

    public void sendTicketConfirmationEmail(String recipient, Long id, String ticketName) throws IOException {
        StringBuilder emailResponse = new StringBuilder();

        emailResponse.append("Dear ").append(recipient).append(",\n\n");
        emailResponse.append("We have received your service request for '").append(ticketName).append("' in good order.\n");
        emailResponse.append("This email is to confirm that your service ticket has been successfully created..\n");
        emailResponse.append("Your ticket number is #").append(id).append("\n");
        emailResponse.append("Our team will begin working on your request shortly.\n\n");
        emailResponse.append("Thank you for choosing our services.\n\n");
        emailResponse.append("Best regards,\nThe Helvar support team");

        Email from = new Email("serviceticketapp@gmail.com");
        String subject = "Confirmation of your service ticket";
        Email to = new Email(recipient);
        Content content = new Content("text/plain", emailResponse.toString());

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void sendTicketUpdate(String recipient, String ticketName, String responseBody) throws IOException {
        StringBuilder emailResponse = new StringBuilder();

        emailResponse.append("Dear ").append(recipient).append(",\n\n");
        emailResponse.append("This email is to inform you there has been an update in your ticket '").append(ticketName).append("'.\n");
        emailResponse.append("the response reads as follows:\n\n");
        emailResponse.append(responseBody).append("\n\n");
        emailResponse.append("Thank you for choosing our services.\n\n");
        emailResponse.append("Best regards,\nThe Helvar support team");

        Email from = new Email("serviceticketapp@gmail.com");
        String subject = "Update of your service ticket";
        Email to = new Email(recipient);
        Content content = new Content("text/plain", emailResponse.toString());

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }
}
