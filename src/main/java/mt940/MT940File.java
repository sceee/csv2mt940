/*
 * Created on 21.09.2004
 */
package mt940;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joscha Feth
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MT940File {

	private static final String sectionDelimiter = "-\r\n";
	private final List<MT940Section> sections = new ArrayList<MT940Section>();

	public void addSection(MT940Section s) {
		sections.add(s);
	}

	@Override
	public String toString() {
		String file = "";
		for (int i = 0; i < sections.size(); i++) {
			file += sections.get(i) + sectionDelimiter;
		}
		return file;
	}
}
