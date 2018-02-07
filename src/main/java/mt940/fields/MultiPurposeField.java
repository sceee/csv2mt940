/*
 * Created on 22.09.2004
 */
package mt940.fields;

import mt940.MT940Field;
import mt940.SplitToMT940Fields;

/**
 * @author Joscha Feth
 */
public class MultiPurposeField extends MT940Field {

	public final int fieldOrder = 7;

	public MultiPurposeField(String bookingCode, String bookingText, String bookingPurpose, String orderingAccNo, String orderingName) {
		setFieldNumber(86);
		String temp = bookingCode + SplitToMT940Fields.START_CHAR + "00" + bookingText;
		if (!bookingPurpose.equals("")) {
			temp += END_FIELD + SplitToMT940Fields.split(bookingPurpose, 20, 27);
		}
		if (!orderingAccNo.equals("")) {
			temp += END_FIELD + SplitToMT940Fields.START_CHAR + "31" + orderingAccNo;
		}
		if (!orderingName.equals("")) {
			temp += END_FIELD + SplitToMT940Fields.split(orderingName, 32, 27);
		}

		setFieldValue(temp);
	}
}
