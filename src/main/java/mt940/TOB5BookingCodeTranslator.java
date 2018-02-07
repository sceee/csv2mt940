package mt940;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on 21.09.2004
 */
/**
 * @author Joscha Feth
 */
public class TOB5BookingCodeTranslator {

	private static final Map<String, BookingCode> codes = new HashMap<String, BookingCode>();

	static {
		codes.put("Lastschrift", new BookingCode("005", "005"));
		codes.put("Überweisung", new BookingCode("020", "040"));
		codes.put("Gutschrift", new BookingCode("051", "051"));
		codes.put("Zinsen", new BookingCode("814", "INT"));
		codes.put("Sollzinsen", new BookingCode("814", "INT"));
		codes.put("Habenzinsen", new BookingCode("814", "INT"));
		codes.put("Solidaritätszuschlag", new BookingCode("805", "MSC"));
		codes.put("Kapitalertragsteuer", new BookingCode("805", "MSC"));
	}

	/*public TOB5BookingCodeTranslator() {

		codes.put("D-AUFTRAG", "008");
		swift.put("D-AUFTRAG", "019");
//		swift.put("D-AUFTRAG","STO");

		codes.put("SB-AUSZAHL", "083");
		//~ also other codes found, like "024"
		swift.put("SB-AUSZAHL", "033");

		codes.put("E-CASH", "005");
		swift.put("E-CASH", "025");

		codes.put("ÜBERWEISG", "020");
		swift.put("ÜBERWEISG", "040");

		codes.put("GUTSCHRIFT", "051");
		swift.put("GUTSCHRIFT", "051");

		codes.put("DA-GUTSCHR", "052");
		swift.put("DA-GUTSCHR", "052");

		codes.put("AUSLD.ZAHL", "065");
		swift.put("AUSLD.ZAHL", "062");

		codes.put("ÜBERTRAG", "820");
//		swift.put("ÜBERTRAG","065");
		swift.put("ÜBERTRAG", "TRF");

		codes.put("DIVIDENDE", "835");
//		swift.put("DIVIDENDE","091");
		swift.put("DIVIDENDE", "DIV");

		codes.put("EINZAHLUNG", "082");
		swift.put("EINZAHLUNG", "080");

		codes.put("ABSCHLUSS", "805");
//		swift.put("ABSCHLUSS","070");
		swift.put("ABSCHLUSS", "INT");

		//~ not sure on the following
		codes.put("AUSZAHLUNG", "084");
//		swift.put("AUSZAHLUNG","084");

		codes.put("SCHECKS", "070");
		swift.put("SCHECKS", "CHK");

		codes.put("DEPOTVERWAHRG", "803");
//		swift.put("DEPOTVERWAHRG", "803");		
	}*/
	public static String getTransactionCode(String fidorBookingString) {
		String ret = "999";
		if (codes.containsKey(fidorBookingString)) {
			return codes.get(fidorBookingString).getCode();
		} else {
			System.out.println("not found: " + fidorBookingString);
		}
		return ret;
	}

	public static String getSwiftCode(String fidorBookingString) {
		String ret = "MSC";
		if (codes.containsKey(fidorBookingString)) {
			ret = codes.get(fidorBookingString).getSwift();
		} else {
			System.out.println("not found: " + fidorBookingString);
		}
		return ret;
	}
}
