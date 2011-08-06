package es.alrocar.map.vector.provider.geom;

import java.util.HashMap;

import es.prodevelop.gvsig.mini.geom.Point;

public class AttributePoint extends Point {

	private HashMap fields = new HashMap();

	public void addField(String key, Object value, int type) {
		fields.put(key, new Attribute(value, type));
	}

	public Attribute getAttribute(String key) {
		return (Attribute) fields.get(key);
	}

	public HashMap getAttributes() {
		return fields;
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

		public int type = TYPE_UNKNOWN;
		public Object value;

		public Attribute(Object value, int type) {
			this.value = value;
			this.type = type;
		}
	}
}
