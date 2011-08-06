package es.alrocar.map.vector.provider.geom;

import java.util.HashMap;

import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.IGeometry;


public class FeatureAttribute extends Feature {

	private HashMap fields = new HashMap();
	private String text;

	public FeatureAttribute(IGeometry geometry) {
		super(geometry);
		// TODO Auto-generated constructor stub
	}

	public void addField(String key, String value, int type) {
		fields.put(key, new Attribute(value, type));
	}

	public Attribute getAttribute(String key) {
		return (Attribute) fields.get(key);
	}

	public HashMap getAttributes() {
		return fields;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addFields(String[] keys, String[] values,
			HashMap<String, Integer> fieldTypes) {
		if (keys != null && values != null && fieldTypes != null) {
			final int length = keys.length;
			if (length != values.length || length != fieldTypes.size())
				return;

			String key;
			for (int i = 0; i < length; i++) {
				key = keys[i];
				if (key != null)
					this.addField(key, values[i], fieldTypes.get(key));
			}
		}
	}

	/**
	 * Get the value object associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return The object associated with the key.
	 * @throws BaseException
	 *             if the key is not found.
	 */
	public Object get(String key) throws BaseException {
		Object o = opt(key);
		if (o == null) {
			throw new BaseException(key + " not found.");
		}
		return o;
	}

	/**
	 * Get the string associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return A string which is the value.
	 * @throws BaseException
	 *             if the key is not found.
	 */
	public String getString(String key) throws BaseException {
		return get(key).toString();
	}

	/**
	 * Get an optional value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return An object which is the value, or null if there is no value.
	 */
	public Object opt(String key) {
		return key == null ? null : this.fields.get(key);
	}

	/**
	 * Get the boolean value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return The truth.
	 * @throws BaseException
	 *             if the value is not a Boolean or the String "true" or
	 *             "false".
	 */
	public boolean getBoolean(String key) throws BaseException {
		Object o = get(key);
		if (o.equals(Boolean.FALSE)
				|| (o instanceof String && ((String) o)
						.equalsIgnoreCase("false"))) {
			return false;
		} else if (o.equals(Boolean.TRUE)
				|| (o instanceof String && ((String) o)
						.equalsIgnoreCase("true"))) {
			return true;
		}
		throw new BaseException(key + "is not a Boolean.");
	}

	/**
	 * Get the double value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return The numeric value.
	 * @throws BaseException
	 *             if the key is not found or if the value is not a Number
	 *             object and cannot be converted to a number.
	 */
	public double getDouble(String key) throws BaseException {
		Object o = get(key);
		try {
			return o instanceof Number ? ((Number) o).doubleValue() : Double
					.valueOf((String) o).doubleValue();
		} catch (Exception e) {
			throw new BaseException(key + " is not a number.");
		}
	}

	/**
	 * Get the int value associated with a key. If the number value is too large
	 * for an int, it will be clipped.
	 * 
	 * @param key
	 *            A key string.
	 * @return The integer value.
	 * @throws BaseException
	 *             if the key is not found or if the value cannot be converted
	 *             to an integer.
	 */
	public int getInt(String key) throws BaseException {
		Object o = get(key);
		return o instanceof Number ? ((Number) o).intValue()
				: (int) getDouble(key);
	}

	public static class Attribute {

		public final static int TYPE_UNKNOWN = -1;
		public final static int TYPE_STRING = 0;
		public final static int TYPE_INT = 1;
		public final static int TYPE_DOUBLE = 2;
		public final static int TYPE_LIST_STRING = 3;
		public final static int TYPE_LIST_INT = 4;
		public final static int TYPE_LIST_DOUBLE = 5;
		public final static int TYPE_URL = 6;
		public final static int TYPE_PHONE = 7;
		public final static int TYPE_DATE = 8;
		public final static int TYPE_IMG = 9;
		public final static int TYPE_IMG_URL = 10;

		public int type = TYPE_UNKNOWN;
		public String value;

		public Attribute(String value, int type) {
			this.value = value;
			this.type = type;
		}
	}

}
