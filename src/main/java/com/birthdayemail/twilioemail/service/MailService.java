package com.birthdayemail.twilioemail.service;

import com.birthdayemail.twilioemail.model.EmailRequest;
import com.sendgrid.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
public class MailService {

    @Autowired
    SendGrid sendGrid;

    public Response sendEmail(EmailRequest emailRequest) {
        Mail mail = new Mail(new Email("rakesh.chavan776@gmail.com"), "Birthday Wishes", new Email(emailRequest.getTo()),new Content("text/plain", "Dear User, You're not getting older, you're just getting wiser. Happy, healthy, wonderful birthday to you, my friend!!!"));
        mail.setReplyTo(new Email("rac16021999@gmail.com"));
        Request request=new Request();

        Response response=new Response();
        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response=this.sendGrid.api(request);

        } catch (IOException exception){
            log.info(exception.getMessage());
        }
        log.info("Mail Service method has been called");
        return  response;
    }
}
