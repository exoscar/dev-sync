package org.devsync.spring.email.mapper;

import org.devsync.spring.auth.entity.User;
import org.devsync.spring.email.dto.EmailRecipient;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public EmailRecipient toEmailRecipient(User user){
        return EmailRecipient.builder().email(user.getEmail()).firstName(user.getFirstName()).build();
    }
}
