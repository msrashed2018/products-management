package com.kiwe.products.models;

import java.io.File;
import java.util.Map;

import lombok.Data;

@Data
public class Mail {
	private String from;
	private String to;
	private String subject;
	private String template;
	private Map<String, Object> model;
//	@ToStringExclude
	private Map<String, File> attachments;

}
