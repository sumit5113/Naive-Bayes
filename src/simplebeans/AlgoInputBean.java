/**
 * 
 */
package simplebeans;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sumit
 *
 */
public class AlgoInputBean {

	private String dataUrl;
	private String targetColumnName;
	private int targetColumnIndex;// 1 based index
	private boolean hasHeader;
	private String filedSeparator;
	private String testDataUrl;
	private Map<Integer, String> featureColumnNames;
	private int numberOfColumn;
	private int lSoothingFactor;

	/**
	 * 
	 */
	public AlgoInputBean() {
		this.featureColumnNames = new HashMap<Integer,String>();
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}

	public int getTargetColumnIndex() {
		return targetColumnIndex;
	}

	public void setTargetColumnIndex(int targetColumnIndex) {
		this.targetColumnIndex = targetColumnIndex;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public String getFiledSeparator() {
		return filedSeparator;
	}

	public void setFiledSeparator(String filedSeparator) {
		this.filedSeparator = filedSeparator;
	}

	public String getTestDataUrl() {
		return testDataUrl;
	}

	public void setTestDataUrl(String testDataUrl) {
		this.testDataUrl = testDataUrl;
	}

	public void addColumnName(String columnName, int index) {
		this.featureColumnNames.put(index,columnName);
	}

	public Map<Integer, String> getColumnNames() {
		return this.featureColumnNames;
	}

	public int getNumberOfColumn() {
		return numberOfColumn;
	}

	public void setNumberOfColumn(int numberOfColumn) {
		this.numberOfColumn = numberOfColumn;
	}

	public int getlSoothingFactor() {
		return lSoothingFactor;
	}

	public void setlSoothingFactor(int lSoothingFactor) {
		this.lSoothingFactor = lSoothingFactor;
	}
}
