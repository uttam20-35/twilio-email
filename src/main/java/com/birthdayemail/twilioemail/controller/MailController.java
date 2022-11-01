package com.birthdayemail.twilioemail.controller;

import com.birthdayemail.twilioemail.model.EmailRequest;
import com.birthdayemail.twilioemail.service.MailService;
import com.sendgrid.Response;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@Log4j2
public class MailController {

    @Value("${topic.name.consumer")
    private String topicName;

    @Autowired
    private MailService mailService;

    @PostMapping(path = "/sendmail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        Response response = mailService.sendEmail(emailRequest);
        if (response.getStatusCode() == 200 || response.getStatusCode() == 202) {
            return new ResponseEntity<>("Send successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("failed to send", HttpStatus.NOT_FOUND);
    }


    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public void consume(ConsumerRecord<String, String> payload) {
        log.info("Topic: {}", topicName);
        log.info("key: {}", payload.key());
        log.info("Headers: {}", payload.headers());
        log.info("Partition: {}", payload.partition());
        log.info("Order: {}", payload.value());

        //We are getting the mail list in double arrays, due to which twilio was unable to send mails to the users,
        //hence we have separated the array list in single array list.
        String emailString = payload.value();
        String replace = emailString.replace("[", "");
        log.info(replace + "After replace [");
        String replace2 = replace.replace("]", "");
        log.info(replace2 + "after replace ]");
        List<String> mailId = new ArrayList<>(Arrays.asList(replace2.split(",")));
        log.info("Array as list " + mailId.toString());


        for (String mails : mailId) {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(mails);
            Date date = new Date();
            log.info("Getting list of mail id's whose birthdate is on {} ",date);
            Response response = mailService.sendEmail(emailRequest);
            log.info("Service method inside controller has been called");
            if (response.getStatusCode() == 200 || response.getStatusCode() == 202) {
                log.info("Your mail has been send successfully to the email id's: {}", mails);
            } else {
                log.info("failed to send mail");
            }
        }


    }

}
