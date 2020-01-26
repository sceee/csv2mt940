/*
 * Created on 21.09.2004
 */
package csvtomt940.mt940.fields;

import csvtomt940.mt940.MT940Field;

/**
 * @author Joscha Feth
 */
public class AccountDesignation extends MT940Field {

	public final int fieldOrder = 3;

	public AccountDesignation(String accountNumber, String bankSortingCode, String currency) {
		setFieldNumber(25);
		// according to db MT940/942 format specifications — version June 2012 page 07, https://www.deutschebank.nl/nl/docs/MT94042_EN.pdf
		setFieldValue(accountNumber + currency);
	}
}
