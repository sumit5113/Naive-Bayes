/**
 * 
 */
package algo;

import java.io.IOException;

import simplebeans.AlgoInputBean;
import simplebeans.AlgoStructBean;
import util.Constants;

/**
 * @author sumit
 *
 */
public class NaiveBayesController {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// 1. create an input parameter for algo
		AlgoStructBean algoStructBean = getAlgoInput();
		// 2. process one by one record from file and update the prior and
		// conditional distribution.
		// 2.1 use laplace transformtion
		// 3. perform predictions on test data
		NaiveBayesOnesVsAllAlgo.start(algoStructBean);
		NaiveBayesAlgo.start(algoStructBean);
	}

	private static AlgoStructBean getAlgoInput() {
		AlgoStructBean algoStructBean = new AlgoStructBean();
		AlgoInputBean algoInputBean = new AlgoInputBean();
		algoStructBean.setAlgoInptBean(algoInputBean);

		algoInputBean
				.setDataUrl("L:/I526 Machine Learning/assignment3/zoo-train.csv");
		algoInputBean
				.setTestDataUrl("L:/I526 Machine Learning/assignment3/zoo-test.csv");
		algoInputBean.setFiledSeparator(",");
		algoInputBean.setHasHeader(false);
		// should be in the order
		algoInputBean.setTargetColumnIndex(17);
		algoInputBean.setTargetColumnName(Constants.DEFAULT_COLUMN_NAME
				+ algoInputBean.getTargetColumnIndex());
		algoInputBean.setNumberOfColumn(17);
		
		for (int i = 1; i <= 17; i++) {
			if (i != algoInputBean.getTargetColumnIndex()) {
				algoInputBean.addColumnName(Constants.DEFAULT_COLUMN_NAME + i,
						i);
			}
		}
		
		algoInputBean.setlSoothingFactor(1);
		return algoStructBean;
	}

}
