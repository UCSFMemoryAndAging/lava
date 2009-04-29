package edu.ucsf.lava.core.type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.propertyeditors.CustomDateEditor;

public class LavaCustomDateEditor extends CustomDateEditor {
	
	public LavaCustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
	}

	public LavaCustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
		super(dateFormat, allowEmpty, exactDateLength);
	}

	// override (see comments above)
	public String getAsText() {
		Object obj = getValue();
		if (obj != null && obj instanceof String) {
			try {
				// assume the default format for Date objects based on Java API JavaDocs to parse into a String
				// (not sure if this can be done automatically, but did not work when using the default
				// DateFormat.getDateInstance().parse((String)obj)  got ParseException
				DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());	
				setValue(formatter.parse((String)obj));
			}
			catch (ParseException pe) {
				// TODO: need logger and log this, and would be good to rethrow unchecked exception here
			}
		}
		return super.getAsText();
	}
	
}
