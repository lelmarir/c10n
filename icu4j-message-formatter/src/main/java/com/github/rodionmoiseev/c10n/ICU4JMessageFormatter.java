package com.github.rodionmoiseev.c10n;

import java.lang.reflect.Method;
import java.util.Locale;

import com.github.rodionmoiseev.c10n.formatters.MessageFormatter;
import com.ibm.icu.text.MessageFormat;

public class ICU4JMessageFormatter implements MessageFormatter{

	@Override
	public String format(Method method, String message, Locale locale, Object... args) {
		return new MessageFormat(message, locale).format( args);
	}

}
