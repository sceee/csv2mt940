/*
 * Created on 21.09.2004
 */
package csvtomt940.mt940.fields;

import csvtomt940.mt940.MT940Field;

/**
 * @author Joscha Feth
 */
public class TransactionReferenceNumber extends MT940Field {

	public final int fieldOrder = 1;

	public TransactionReferenceNumber(String value) {
		setFieldNumber(20);
		setFieldValue(value);
	}

	public TransactionReferenceNumber() {
		setFieldNumber(20);
		setFieldValue("");
	}
}
