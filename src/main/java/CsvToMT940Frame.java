
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import mt940.MT940File;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

/*
 * Created on 21.09.2004
 */
/**
 * @author Joscha Feth
 */
public class CsvToMT940Frame extends JFrame {

	final static long serialVersionUID = 0;
	private Properties config;

	private JPanel jContentPane;

	private JPanel jPanelInput;
	private JTextField jBankSortingCode;
	private JTextField jAccountNumber;
	private JFormattedTextField jClosingBalance;
	private JTextField jCurrency;
	private JTextField jFilename;
	private JPanel jPanelButtons;
	private JButton jButtonLoad;
	private JButton jButtonSave;
	private JFileChooser jFileChooserOpen = new JFileChooser();
	private JFileChooser jFileChooserSave = new JFileChooser();
	private CsvToMT940 c2m = new CsvToMT940(this);
	private JPanel jPanelInfo;
	private JTextArea jTextArea;
	private JScrollPane jScrollPane;
	private CsvFilter cf = new CsvFilter();
	private MT940Filter mf = new MT940Filter();

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		JFrame frame = new CsvToMT940Frame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Display the window.
		//frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public CsvToMT940Frame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		// parse ini file
		String pathSeparator = System.getProperty("file.separator"),
				configFilename = System.getProperty("user.home") + pathSeparator + ".CSV2MT940" + pathSeparator + "config.ini";
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
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			} finally {
				IOUtils.closeQuietly(configFileStream);
				IOUtils.closeQuietly(isr);
			}
		}

		setMinimumSize(new Dimension(500, 300));
		setSize(1000, 500);
		setContentPane(getJContentPane());
		setTitle("CSV-zu-MT940-Konverter");
		jFileChooserOpen.setFileFilter(cf);
		String directory = getConfigParam("directory");
		jFileChooserSave.setFileFilter(mf);
		if (!"".equals(directory)) {
			File startDir = new File(directory);
			jFileChooserOpen.setCurrentDirectory(startDir);
			jFileChooserSave.setCurrentDirectory(startDir);
		}
		jContentPane.revalidate();
		jPanelInfo.revalidate();
		jScrollPane.revalidate();
	}

	private String getConfigParam(String key) {
		Object value = config.get(key);
		return (value != null ? value.toString() : "");
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelInput(), BorderLayout.NORTH);
			jContentPane.add(getJPanelButtons(), BorderLayout.CENTER);
			jContentPane.add(getJPanelInfo(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JPanel getJPanelInput() {
		if (jPanelInput == null) {
			jPanelInput = new JPanel();
			jPanelInput.setLayout(new GridLayout(0, 4, 5, 3));
			jPanelInput.add(new JLabel("IBAN"), null);
			jPanelInput.add(getJAccountNumber(), null);

			jPanelInput.add(new JLabel("BIC"), null);
			jPanelInput.add(getJBankSortingCode(), null);

			jPanelInput.add(new JLabel("Endsaldo"), null);
			jPanelInput.add(getJClosingBalance(), null);

			jPanelInput.add(new JLabel("Währung"), null);
			jPanelInput.add(getJCurrency(), null);
		}
		return jPanelInput;
	}

	private JTextField getJAccountNumber() {
		if (jAccountNumber == null) {
			jAccountNumber = new JTextField();
			jAccountNumber.setText(getConfigParam("iban"));
			jAccountNumber.setColumns(24);
		}
		return jAccountNumber;
	}

	public String getAccountNumber() {
		return jAccountNumber.getText().replace(" ", "");
	}

	private JTextField getJBankSortingCode() {
		if (jBankSortingCode == null) {
			jBankSortingCode = new JTextField();
			jBankSortingCode.setText(getConfigParam("bic"));
			jBankSortingCode.setColumns(16);
		}
		return jBankSortingCode;
	}

	public String getBankSortingCode() {
		return jBankSortingCode.getText();
	}

	private JFormattedTextField getJClosingBalance() {
		if (jClosingBalance == null) {
			NumberFormat amountFormat = NumberFormat.getNumberInstance();
			amountFormat.setMaximumFractionDigits(2);
			amountFormat.setMinimumFractionDigits(2);
			amountFormat.setMinimumIntegerDigits(1);
			jClosingBalance = new JFormattedTextField(amountFormat);
			jClosingBalance.setColumns(10);
		}
		return jClosingBalance;
	}

	public boolean isFormValid() {
		return getClosingBalance() != null;
	}

	public Double getClosingBalance() {
		Object value = jClosingBalance.getValue();
		return (value instanceof Number ? ((Number) value).doubleValue() : null);
	}

	private JTextField getJCurrency() {
		if (jCurrency == null) {
			jCurrency = new JTextField();
			jCurrency.setText("EUR");
		}
		return jCurrency;
	}

	public String getCurrency() {
		return jCurrency.getText();
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return JPanel
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setMinimumSize(new Dimension(400, 80));
			jPanelButtons.add(getJButtonLoad(), null);
			jPanelButtons.add(getJButtonSave(), null);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return JButton
	 */
	private JButton getJButtonLoad() {
		if (jButtonLoad == null) {
			jButtonLoad = new JButton();
			jButtonLoad.setText("CSV-Datei öffnen");
			jButtonLoad.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jFileChooserOpen.showOpenDialog(jContentPane) == JFileChooser.APPROVE_OPTION) {
						File f = jFileChooserOpen.getSelectedFile();
						jFilename.setText(f.getAbsolutePath());
						jFileChooserOpen.setCurrentDirectory(f);
						jButtonSave.setEnabled(true);
						c2m.loadCSV(f);
						addInfo("\"" + f.getName() + "\" geladen.");
						addInfo(c2m.getCount() + " Datensätze.");
						doSave();
					}
				}

			});
		}
		return jButtonLoad;
	}

	public void addInfo(String info) {
		jTextArea.append("\n" + info);
	}

	/**
	 * This method initializes jButtonSave
	 *
	 * @return JButton
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setText("MT940-Datei speichern");
			jButtonSave.setEnabled(false);
			jButtonSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doSave();
				}
			});
		}
		return jButtonSave;
	}

	protected void doSave() {
		if (isFormValid()) {
			try {
				MT940File f = c2m.createMT940();
				if (f != null) {
					File currentDirectory = jFileChooserSave.getCurrentDirectory();
					String filename = c2m.getFilename();
					if (!"".equals(filename)) {
						jFileChooserSave.setSelectedFile(new File(currentDirectory, filename));
					}
					if (jFileChooserSave.showSaveDialog(jContentPane) == JFileChooser.APPROVE_OPTION) {
						File sf = jFileChooserSave.getSelectedFile();
						jFileChooserSave.setCurrentDirectory(sf);

						OutputStreamWriter fw = null;
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(sf);
							fw = new OutputStreamWriter(fos, Charsets.ISO_8859_1);
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
					} else {
						addInfo("Konvertierung vom Benutzer abgebrochen!");
					}
				}
			} catch (Exception ex) {
				addInfo("Fehler: " + ex.getMessage());
			}
		} else {
			addInfo("Bitte geben Sie den Endsaldo an.");
		}

	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return JPanel
	 */
	private JPanel getJPanelInfo() {
		if (jPanelInfo == null) {
			jPanelInfo = new JPanel();
			jPanelInfo.setLayout(new BoxLayout(jPanelInfo, BoxLayout.Y_AXIS));
			jPanelInfo.add(getJFilename(), null);
			jPanelInfo.add(getJScrollPane(), null);
		}
		return jPanelInfo;
	}

	/**
	 * This method initializes jTextArea
	 *
	 * @return JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setText("Für Fidor angepaßt durch Felix Rudolphi, basierend auf einem ähnlichen Tool von Joscha Feth, www.feth.com, Nutzung auf eigene Gefahr\nBitte öffnen Sie eine Datei!");
			jTextArea.setAutoscrolls(true);
			jTextArea.setEditable(false);
			jTextArea.setLineWrap(true);
//			jTextArea.setMargin(new Insets(5,5,5,5));
		}
		return jTextArea;
	}

	private JTextField getJFilename() {
		if (jFilename == null) {
			jFilename = new JTextField();
		}
		return jFilename;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
			jScrollPane.setPreferredSize(new Dimension(0, 200));
//			jScrollPane.setAutoscrolls(true);
		}
		return jScrollPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
