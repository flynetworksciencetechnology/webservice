package com.flypay.service;

import java.util.List;

import com.flypay.model.Mail;

public interface MailService {

	void save(Mail mail, List<String> toUser);
}
