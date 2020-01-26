/*
 * Created on 21.09.2004
 */
package csvtomt940.mt940.fields;

import csvtomt940.mt940.MT940Field;

/**
 * @author Joscha Feth
 */
public class AccountStatementNumber extends MT940Field {

	public final int fieldOrder = 4;

	/**
	 *
	 * @param accountStatementNumber
	 * @param pageNumber	starts with 1
	 */
	public AccountStatementNumber(String accountStatementNumber, String pageNumber) {
		setFieldNumber("28C");
		String temp = accountStatementNumber;
		if (!pageNumber.equals("")) {
			temp += "/" + pageNumber;
		}
		setFieldValue(temp);
	}

	public AccountStatementNumber() {
		setFieldNumber("28C");
		setFieldValue(0);
	}
}
