/*
 * Created on 21.09.2004
 */
package mt940.fields;

import mt940.MT940Field;

/**
 * @author Joscha Feth
 */
public class RelatedReferenceNumber extends MT940Field {

	public final int fieldOrder = 2;

	public RelatedReferenceNumber(String value) {
		setFieldNumber(21);
		setFieldValue(value);
	}

	public RelatedReferenceNumber() {
		setFieldNumber(21);
		setFieldValue("");
	}
}
