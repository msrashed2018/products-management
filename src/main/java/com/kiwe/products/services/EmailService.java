package com.kiwe.products.services;

import java.io.IOException;

import javax.mail.MessagingException;

import com.kiwe.products.models.Mail;

import freemarker.template.TemplateException;

public interface EmailService {
	boolean sendEmail(Mail mail) throws MessagingException, IOException, TemplateException;

}
