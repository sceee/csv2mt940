/*
 * Created on 21.09.2004
 */
package csvtomt940.mt940.fields;

import java.util.Date;
import csvtomt940.mt940.MT940Field;

/**
 * @author Joscha Feth
 */
public class TurnOverLine extends MT940Field {

	public final int fieldOrder = 6;
	private String emptyRef = "0000000000000000";

	/**
	 *
	 * @param valueDate
	 * @param bookingDate
	 * @param isCredit
	 * @param isCancelled
	 * @param currency
	 * @param amount
	 * @param swiftCode
	 * @param customerRef
	 * @param bankRef
	 */
	public TurnOverLine(Date valueDate, Date bookingDate, boolean isCredit, boolean isCancelled, String currency, Number amount, String swiftCode, String customerRef, String bankRef) {
		setFieldNumber(61);
		String temp = MT940Field.lFormat.format(valueDate) + MT940Field.sFormat.format(bookingDate);
		if (isCancelled) {
			temp += "R";
		}
		if (isCredit) {
			temp += "C";
		} else {
			// is debit
			temp += "D";
		}
		temp += currency.charAt(2) + MT940Field.dFormat.format(amount.floatValue()) + "N" + swiftCode;
		if (customerRef.equals("")) {
			temp += emptyRef;
		} else {
			temp += customerRef;
		}
		temp += "//";
		if (bankRef.equals("")) {
			temp += emptyRef;
		} else {
			temp += bankRef;
		}
		//~ SUB FIELD 9 MISSING
		setFieldValue(temp);
	}
}
