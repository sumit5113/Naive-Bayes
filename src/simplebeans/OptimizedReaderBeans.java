/**
 * 
 */
package simplebeans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author sumit
 *
 */
public class OptimizedReaderBeans {
	private String dataUrl;
	private boolean isClosed;
	BufferedReader rdr = null;

	public OptimizedReaderBeans(String pDataUrl) {
		this.dataUrl = pDataUrl;

	}

	// return null to represent the end of file
	public String readNextLine() {
		if (this.rdr == null && !isClosed()) {
			try {
				this.rdr = new BufferedReader(new FileReader(this.dataUrl));
			} catch (IOException ex) {
				this.rdr = null;
				this.isClosed = true;
				throw new IllegalStateException(ex);
			}
		}

		String text = null;
		try {
			text = this.rdr.readLine();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			if (text == null) {
				close();
			}
		}

		return text;
	}

	private boolean isClosed() {
		return this.isClosed;
	}

	public void close() {
		if (this.rdr == null || isClosed())
			return;
		try {
			this.rdr.close();
		} catch (IOException ex) {
			this.isClosed = true;
			this.rdr = null;
			throw new IllegalStateException(ex);
		}
	}
}
