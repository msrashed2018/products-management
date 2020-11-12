package com.kiwe.products.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static void formatDates(Calendar start, Calendar end, String startDate, String endDate)
			throws ParseException {

		// creating a date object with specifed time.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		start.setTime(sdf.parse(startDate));
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);

		end.setTime(sdf.parse(endDate));
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 0);

	}

	public static String convertLocalDateToArabicString(LocalDate date) {
		Locale arabicLocale = Locale.forLanguageTag("ar");
		DecimalStyle arabicDecimalStyle = DecimalStyle.of(arabicLocale).withZeroDigit('٠');
		DateTimeFormatter arabicDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(arabicLocale)
				.withDecimalStyle(arabicDecimalStyle);
		return date.format(arabicDateFormatter);
	}

	public static Date convertEpochTimeToDate(String epochTime) {
		long seconds = Long.parseLong(epochTime);
		return new Date(seconds);
	}

	public static String convertDateToArabicString(Date date) {
		Locale arabicLocale = Locale.forLanguageTag("ar");
		DecimalStyle arabicDecimalStyle = DecimalStyle.of(arabicLocale).withZeroDigit('٠');
		DateTimeFormatter arabicDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(arabicLocale)
				.withDecimalStyle(arabicDecimalStyle);
		String dateString = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(arabicDateFormatter);
		return dateString;
	}

	public static String convertLocalDateToEnglishString(LocalDate date) {
		Locale englishLocale = Locale.forLanguageTag("en");
		DateTimeFormatter englishDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(englishLocale);
		return date.format(englishDateFormatter);
	}

	public static String convertDateToEnglishString(Date date) {
		Locale englishLocale = Locale.forLanguageTag("en");
		DateTimeFormatter englishDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(englishLocale);
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(englishDateFormatter);
	}

	public static int getRemaingMonths() {
		return 12 - LocalDate.now().getMonthValue();
	}
}
