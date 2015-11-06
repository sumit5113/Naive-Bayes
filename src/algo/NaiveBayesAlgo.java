/**
 * 
 */
package algo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import simplebeans.AlgoInputBean;
import simplebeans.AlgoOutputBean;
import simplebeans.AlgoStructBean;
import simplebeans.NaiveModel;
import simplebeans.QueryBean;
import util.ProbabilityUtil;

/**
 * @author sumit
 *
 */
public class NaiveBayesAlgo {

	public static void start(AlgoStructBean algoStructBean) throws IOException {
		// 1.read and populate the table
		readAndPopulateProbTable(algoStructBean);
		predictAccuracy(algoStructBean);
	}

	private static void readAndPopulateProbTable(AlgoStructBean algoStructBean)
			throws IOException {
		//
		AlgoInputBean algoInputBean = algoStructBean.getAlgoInptBean();
		AlgoOutputBean algoOutputBean = new AlgoOutputBean();
		NaiveModel naiveModel = algoOutputBean.getLearnedModel();
		naiveModel.setlSoothing(algoInputBean.getlSoothingFactor());

		try (BufferedReader rdr = new BufferedReader(new FileReader(
				algoInputBean.getDataUrl()))) {
			String text = null;

			while ((text = rdr.readLine()) != null) {
				String[] splittedText = text.split(algoInputBean
						.getFiledSeparator());
				for (int i = 0; i < splittedText.length; i++) {
					if (i + 1 == algoInputBean.getTargetColumnIndex()) {
						naiveModel.addToPriorProb(splittedText[i], 1);
						continue;
					}
					// populating tables
					naiveModel
							.addToTable(
									algoInputBean.getColumnNames().get(i + 1),
									algoInputBean.getTargetColumnName(),
									splittedText[i], splittedText[algoInputBean
											.getTargetColumnIndex() - 1], 1, 0);
				}
			}
		}

		// normalize all probabilities
		naiveModel.normalizePriorProb();
		naiveModel.normalizeConditionProb();

		algoStructBean.setAlgoOutputBean(algoOutputBean);
	}

	public static void predictAccuracy(AlgoStructBean algoStructBean)
			throws IOException {
		// read each record from a file and predict the class probabilities
		AlgoInputBean algoInptBean = algoStructBean.getAlgoInptBean();
		AlgoOutputBean algoOutputBean = algoStructBean.getAlgoOutputBean();
		int success = 0;
		int total = 0;
		Map<String, Map<String, Integer>> confusionMatrix = new TreeMap<>();
		try (BufferedReader rdr = new BufferedReader(new FileReader(
				algoInptBean.getTestDataUrl()))) {
			String text = null;

			while ((text = rdr.readLine()) != null) {
				String[] splittedText = text.split(algoInptBean
						.getFiledSeparator());
				total++;
				QueryBean qb = new QueryBean();
				for (int i = 0; i < splittedText.length; i++) {
					if (i + 1 == algoInptBean.getTargetColumnIndex())
						continue;
					qb.addQueryParam(algoInptBean.getColumnNames().get(i + 1),
							splittedText[i]);
				}
				String predictedClass = algoOutputBean.getPrediction(qb,
						algoInptBean.getTargetColumnName());

				String targetClassValue = splittedText[algoInptBean
						.getTargetColumnIndex() - 1];
				if (predictedClass.equals(targetClassValue)) {
					success++;
				} else {
					/*
					 * System.out.println("Mismatched :: " + text +
					 * "[predicted =" + predictedClass + ", actual =" +
					 * targetClassValue + "]");
					 */
				}
				// updating the confusion matrix
				if (!confusionMatrix.containsKey(targetClassValue)) {
					confusionMatrix.put(targetClassValue,
							new TreeMap<String, Integer>());
				}
				if (!confusionMatrix.get(targetClassValue).containsKey(
						predictedClass)) {
					confusionMatrix.get(targetClassValue)
							.put(predictedClass, 0);
				}
				int c = confusionMatrix.get(targetClassValue).get(
						predictedClass);
				confusionMatrix.get(targetClassValue)
						.put(predictedClass, c + 1);
			}
		}
		//
		System.out.println("--------Global Accuraccy (without one vs all)-----------");
		System.out.println("START::");
		ProbabilityUtil.printConfusionMatrix(total, success, confusionMatrix);
		System.out.println("END::");
		
	}
}
