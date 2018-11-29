package br.com.marcospcruz.gestorloja.log.appender;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;

public class CustomFileAppender extends FileAppender {
	@Override
	public void setFile(String fileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (fileName.indexOf("%currentDay") >= 0) {
			fileName = fileName.replaceAll("%currentDay", sdf.format(new Date()));
		}
		super.setFile(fileName);
	}

}
