/*
 * Created on 22.09.2004
 */
package csvtomt940.mt940.fields;

import csvtomt940.mt940.MT940Field;
import csvtomt940.mt940.SplitToMT940Fields;

/**
 * @author Joscha Feth
 */
public class MultiPurposeField extends MT940Field {

	public final int fieldOrder = 7;

	public MultiPurposeField(String bookingCode,String bookingText,String bookingPurpose,String orderingName) {
		this.setFieldNumber(86);
		String temp = bookingCode+"?00"+bookingText+END_FIELD;
		temp += SplitToMT940Fields.split(bookingPurpose,20,27);
		if(!orderingName.equals("")) {
			temp += END_FIELD+SplitToMT940Fields.split(orderingName,32,27);
		}
		
		this.setFieldValue(temp);
	}
}
