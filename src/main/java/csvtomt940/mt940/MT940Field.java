/*
 * Created on 21.09.2004
 */
package csvtomt940.mt940;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

/**
 * @author Joscha Feth
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class MT940Field {

	protected static final char FIELD_DELIMITER = ':';
	protected static final String END_FIELD = "\r\n";
	protected static final SimpleDateFormat lFormat = new SimpleDateFormat("yyMMdd");
	protected static final SimpleDateFormat sFormat = new SimpleDateFormat("MMdd");
	protected static final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	protected static final DecimalFormat dFormat = new DecimalFormat("#0.00", dfs);
	private String fieldNumber;
	private String fieldValue;

	public MT940Field() {
		dfs.setDecimalSeparator(',');
		dFormat.setNegativePrefix("");
	}

	@Override
	public String toString() {
		return FIELD_DELIMITER + fieldNumber + FIELD_DELIMITER + fieldValue + END_FIELD;
	}

	protected void setFieldNumber(int number) {
		setFieldNumber(String.valueOf(number));
	}

	protected void setFieldNumber(String number) {
		this.fieldNumber = number;
	}

	protected void setFieldValue(String value) {
		this.fieldValue = value;
	}

	protected void setFieldValue(int value) {
		setFieldValue(String.valueOf(value));
	}
}
