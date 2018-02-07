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
public class MT940Section {

	private final List<MT940Field> fields = new ArrayList<MT940Field>();

	public void addField(MT940Field f) {
		fields.add(f);
	}

	@Override
	public String toString() {
		String temp = "";
		for (int i = 0; i < fields.size(); i++) {
			temp += fields.get(i);
		}
		return temp;
	}
}
