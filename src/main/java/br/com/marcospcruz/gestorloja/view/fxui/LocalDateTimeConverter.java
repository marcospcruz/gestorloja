package br.com.marcospcruz.gestorloja.view.fxui;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter {

	public class LocalDateAttributeConverter implements AttributeConverter<LocalDateTime, Date> {

		@Override
		public Date convertToDatabaseColumn(LocalDateTime locDate) {
			return (locDate == null ? null : Date.from(locDate.atZone(ZoneId.systemDefault()).toInstant()));
		}

		@Override
	    public LocalDateTime convertToEntityAttribute(Date date) {
	    	return (date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	    }
	}
}
