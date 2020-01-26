/*
 * Created on 21.09.2004
 */
package csvtomt940.mt940.fields;

import java.util.Date;
import csvtomt940.mt940.MT940Field;

/**
 * @author Joscha Feth
 */
public class OpeningBalance extends MT940Field {

	public final int fieldOrder = 5;

	/**
	 *
	 * @param isOpeningBalance if false an interim balance is created
	 * @param isCredit
	 * @param bookingDate
	 * @param currency	the currency code in ISO 4217 format
	 * @param amount	the amount with a comma as a 'decimal point'
	 */
	public OpeningBalance(boolean isOpeningBalance, boolean isCredit, Date bookingDate, String currency, Number amount) {
		if (isOpeningBalance) {
			setFieldNumber("60F");
		} else {
			//  is interim balance
			setFieldNumber("60M");
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
