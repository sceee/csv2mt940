/*
 * Created on 21.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package csvtomt940;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import csvtomt940.mt940.MT940File;
import csvtomt940.mt940.MT940Section;
import csvtomt940.mt940.TOB5BookingCodeTranslator;
import csvtomt940.mt940.fields.AccountDesignation;
import csvtomt940.mt940.fields.AccountStatementNumber;
import csvtomt940.mt940.fields.ClosingBalance;
import csvtomt940.mt940.fields.MultiPurposeField;
import csvtomt940.mt940.fields.OpeningBalance;
import csvtomt940.mt940.fields.TransactionReferenceNumber;
import csvtomt940.mt940.fields.TurnOverLine;

/**
 * @author Joscha Feth
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CsvToMT940 {

	private static final int MAX_BOOKING_TEXT = 27;
	private static final SimpleDateFormat inFormat = new SimpleDateFormat("dd.MM.yyyy");
	private static final CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';');
	private static String[][] csvData;
	private static Date startDate;

	private static Properties config;
	private static String inputFile;
	private static String outputFile;
	private static String iban;
	private static String bic;
	private static String currency;
	private static double closingBalance;

	/* public static String autoDecode(byte[] bytes) {
		// fix charset
		CharsetDetector charsetDetector = new CharsetDetector();
		charsetDetector.setText(bytes);

		// fix for mis-detection
		CharsetMatch[] detectAll = charsetDetector.detectAll();
		String encodingName = null;
		for (int i = 0; i < detectAll.length; i++) {
			CharsetMatch detect = detectAll[i];
			encodingName = detect.getName();
			int confidence = detect.getConfidence();
			if ("Big5".equals(encodingName)) { // higher confidence required
				if (i * 2 < detectAll.length) {
					// enough other options
					continue;
				}
				confidence -= 15;
			}
			if (confidence > 50) {
				break;
			}
			if (encodingName.startsWith("ISO-8859")
					|| encodingName.startsWith("windows-125")) {
				break;
			}
		}

		// ltr/rtl suffixes not supported
		encodingName = StringUtils.removeEnd(encodingName, "_ltr");
		encodingName = StringUtils.removeEnd(encodingName, "_rtl");
		try {
			return new String(bytes, encodingName);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(CsvToMT940.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new String(bytes);
	} */
	public static void execute(double closingBal) {
		// parse ini file
		File jarPath=new File(CsvToMT940.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String configFilename = jarPath.getParentFile().getAbsolutePath() + "\\config.ini"; // System.getProperty("user.home") + pathSeparator + ".CSV2MT940" + pathSeparator + "config.ini";
		System.out.println("Trying to read " + configFilename);
		File configFile = new File(configFilename);
		config = new Properties();
		if (configFile.canRead()) {
			FileInputStream configFileStream = null;
			InputStreamReader isr = null;
			try {
				configFileStream = new FileInputStream(configFile);
				isr = new InputStreamReader(configFileStream, "UTF-8");
				config.load(isr);
			} catch (IOException ex) {
				Logger.getLogger(CsvToMT940.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				IOUtils.closeQuietly(configFileStream);
				IOUtils.closeQuietly(isr);
			}
		}

		Logger logger = Logger.getLogger(CsvToMT940.class.getName());

		// Load input directory
		inputFile = getConfigParam("inputcsv");
		if (inputFile == null || inputFile.isEmpty()) {
			logger.log(Level.SEVERE, "No input csv file provided in config.ini, aborting!");
			return;
		}

		// Load input directory
		outputFile = getConfigParam("outputfile");
		if (outputFile == null || outputFile.isEmpty()) {
			logger.log(Level.SEVERE, "No output file provided in config.ini, aborting!");
			return;
		}

		// Account number
		iban = getConfigParam("iban");
		if (iban == null || iban.isEmpty()) {
			logger.log(Level.SEVERE, "No iban provided in config.ini, aborting!");
			return;
		}

		// BankSortingCode
		bic = getConfigParam("iban");
		if (bic == null || bic.isEmpty()) {
			logger.log(Level.SEVERE, "No bic provided in config.ini, aborting!");
			return;
		}

		// Currency
		currency = getConfigParam("currency");
		if (currency == null || currency.isEmpty()) {
			logger.log(Level.SEVERE, "No currency provided in config.ini, aborting!");
			return;
		}

		// Closing Balance
		closingBalance = closingBal;

		// Start conversion
		convert();
	}

	private static void convert() {
		File f = new File(inputFile);
		loadCSV(f);
		addInfo("\"" + f.getName() + "\" geladen.");
		addInfo(getCount() + " Datensätze.");
		doSave();
	}

	private static void doSave() {
		try {
			MT940File f = createMT940();
			if (f != null) {
				String filename = outputFile; // getFilename();

				File sf = new File(filename);

				OutputStreamWriter fw = null;
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(sf);
					fw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
					fw.write(f.toString());
					fw.close();
					addInfo("Erfolgreich konvertiert.");
				} catch (IOException e1) {
					e1.printStackTrace();
					addInfo("Fehler! Bitte in die Java-Konsole schauen.");
				} finally {
					IOUtils.closeQuietly(fw);
					IOUtils.closeQuietly(fos);
				}
			}
		} catch (Exception ex) {
			addInfo("Fehler: " + ex.getMessage());
		}
	}

	public static void addInfo(String info) {
		System.out.println(info);
		Logger.getLogger(CsvToMT940.class.getName()).log(Level.INFO, info);
	}

	private static String getConfigParam(String key) {
		Object value = config.get(key);
		return (value != null ? value.toString() : "");
	}

	public static int getCount() {
		return csvData.length - 1; // 1st line is headline
	}

	public static void loadCSV(File csvfile) {
		FileInputStream fis = null;
		//String csvdata = null;
		try {
			fis = new FileInputStream(csvfile);
			//csvdata = autoDecode(IOUtils.toByteArray(fis));
			//CSVParser parser = CSVParser.parse(csvdata, csvFormat);

			CSVParser parser = CSVParser.parse(fis, StandardCharsets.UTF_8, csvFormat);
			List<CSVRecord> records = parser.getRecords();

			int recordCount = records.size();
			csvData = new String[recordCount][];
			for (int i = 0; i < recordCount; i++) {
				CSVRecord record = records.get(i);

				int columnCount = record.size();
				csvData[i] = new String[columnCount];
				for (int j = 0; j < columnCount; j++) {
					csvData[i][j] = record.get(j);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	public void writeMT940(File mt940file) {
		createMT940();
	}

	public String getFilename() {
		if (startDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + ".txt";
		}
		return "";
	}

	public static Double getClosingBalance() {
		return closingBalance;
	}

	public static MT940File createMT940() {
		MT940File m = new MT940File();
		// NumberFormat nf = NumberFormat.getInstance();

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DecimalFormat df = new DecimalFormat("#0.00", dfs);
		df.setPositivePrefix("+");
		String checkDate = "";
		MT940Section ms = new MT940Section();
		// m.addSection(ms);
		int statementNumber = 1;

		try {
			double ob = df.parse(csvData[1][3]).doubleValue();
			// System.out.println("Schlusssaldo:"+ob);
			for (int a = 1; a < csvData.length
					&& csvData[1][2].equals(csvData[a][2]); a++) {
				double ab = df.parse(csvData[a][10]).doubleValue();
				// System.out.println("Kontoänderung:"+ab);
				ob -= ab;
			}
			Number openingBalance = new Double(ob);
			// System.out.print(ob);
			TOB5BookingCodeTranslator tob = new TOB5BookingCodeTranslator();

			for (int i = 1; i < csvData.length; i++) {
				String accountNumber = csvData[i][0];
				String bankSortingCode = csvData[i][1];
				// Number openingBalance = df.parse(csvData[i][3]);
				Date bookingDate = inFormat.parse(csvData[i][5]);
				String balanceCurrency = csvData[i][4];
				Number closingBalance = df.parse(csvData[i][3]);

				if (!checkDate.equals(csvData[i][2])) {
					ms.addField(new TransactionReferenceNumber());
					// ms.addField(new RelatedReferenceNumber());
					ms.addField(new AccountDesignation(accountNumber,
							bankSortingCode, currency));
					// ms.addField(new AccountStatementNumber("" +
					// statementNumber, ""));
					ms.addField(new AccountStatementNumber());
					ms.addField(new OpeningBalance(true, (openingBalance
							.doubleValue() > 0), bookingDate, balanceCurrency,
							openingBalance));
					checkDate = csvData[i][2];
				}
				
				for (; i < csvData.length; i++) {

					System.out.println(csvData[i][5]);

					if (!checkDate.equals(csvData[i][2])) {
						i--;
						checkDate = csvData[i][2];
						continue;
					}
				

					Date valueDate = inFormat.parse(csvData[i][6]);
					String bookingText = csvData[i][7];
					String bookingCode = tob.getTransactionCode(csvData[i][7]);
					String swiftCode = tob.getSwiftCode(csvData[i][7]);
					String bookingPurpose = csvData[i][8];
					String orderingName = csvData[i][9];
					Number amount = df.parse(csvData[i][10]);
					String amountCurrency = csvData[i][11];
					bookingDate = inFormat.parse(csvData[i][5]);

					ms.addField(new TurnOverLine(valueDate, bookingDate,
							(amount.doubleValue() > 0), false, amountCurrency,
							amount, swiftCode, "", ""));
					ms.addField(new MultiPurposeField(bookingCode, bookingText,
							bookingPurpose, orderingName));
				}
				ms.addField(new ClosingBalance(true, (closingBalance
						.doubleValue() > 0), bookingDate, balanceCurrency,
						closingBalance));
				m.addSection(ms);
				ms = new MT940Section();
				statementNumber++;
				openingBalance = closingBalance;
			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			aiobe.printStackTrace();
			addInfo("Keine gültige CSV-Datei! Abbruch...");
			return null;

		} catch (ParseException e) {
			e.printStackTrace();
			addInfo("Parsing-Fehler! Abbruch...");
			return null;
		}
		return m;
	}
}
