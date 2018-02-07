
import java.io.File;

/*
 * Created on 21.09.2004
 */
/**
 * @author Joscha Feth
 */
public class MT940Filter extends FileActions {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String extension = getExtension(f);
		if (extension != null) {
			if (extension.equals("pcc") || extension.equals("940") || extension.equals("txt")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "940 (*.940), PCC (*.pcc) und TXT (*.txt) Dateien";
	}

}
