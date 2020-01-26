/*
 * Created on 22.09.2004
 */
package csvtomt940.mt940.fields;

import csvtomt940.mt940.MT940Field;

/**
 * @author Joscha Feth
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ClosingBalance extends MT940Field {

	public final int fieldOrder = 8;

	/**
	 *
	 * @param isClosingBalance if false an interim balance is created
	 * @param isCredit
	 * @param bookingDate
	 * @param currency	the currency code in ISO 4217 format
	 * @param amount	the amount with a comma as a 'decimal point'
	 */
	public ClosingBalance(boolean isClosingBalance, boolean isCredit, java.util.Date bookingDate, String currency, Number amount) {
		if (isClosingBalance) {
			setFieldNumber("62F");
		} else {
			//  is interim balance
			setFieldNumber("62M");
		}

		String temp = "";
		if (isCredit) {
			temp += "C";
		} else {
			// is debit
			temp += "D";
		}

		temp += MT940Field.lFormat.format(bookingDate) + currency + MT940Field.dFormat.format(amount.floatValue());
		setFieldValue(temp);
	}
}
