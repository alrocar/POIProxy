package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss";

	private String format;

	/**
	 * Default format {@link DateTypeAdapter#DEFAULT_FORMAT}
	 */
	public DateTypeAdapter() {
		this(DEFAULT_FORMAT);
	}

	public DateTypeAdapter(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.value((String) null);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String val = sdf.format(value);
			out.value(val);
		}
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		String result = in.nextString();
		if (result.isEmpty()) {
			return null;
		}
		Date date = DateUtil.stringToDate(result);
		return date;
	}

}
