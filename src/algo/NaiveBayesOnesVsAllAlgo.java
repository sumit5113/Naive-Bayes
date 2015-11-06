/**
 * 
 */
package algo;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import simplebeans.AlgoInputBean;
import simplebeans.AlgoOutputBean;
import simplebeans.AlgoStructBean;
import simplebeans.NaiveModel;
import simplebeans.OptimizedReaderBeans;
import simplebeans.QueryBean;
import util.ProbabilityUtil;

/**
 * @author sumit
 *
 */
public class NaiveBayesOnesVsAllAlgo {

	public static void start(AlgoStructBean algoStructBean) throws IOException {
		// 1.read and populate the table
		Set<String> classProcessedSoFar = new LinkedHashSet<>();
		String curTargetValue = null;
		Map<Integer, Map<Boolean, Integer>> totalTestAccuracyMatrix = new LinkedHashMap<>();

		while ((curTargetValue = readAndPopulateProbTable(algoStructBean,
				classProcessedSoFar)) != null) {

			// prediction on test data based on the current one-vs-all
			predictAccuracy(algoStructBean, curTargetValue,
					totalTestAccuracyMatrix);
			// classProcessedSoFar.clear();
		}
		// System.out.println(totalTestAccuracyMatrix);
		printFinalMissClassification(totalTestAccuracyMatrix);
	}

	private static void printFinalMissClassification(
			Map<Integer, Map<Boolean, Integer>> totalTestAccuracyMatrix) {
		final int[] counts = new int[] { 0, 0 };

		totalTestAccuracyMatrix.forEach((k, v) -> {
			int t = 0;
			v.forEach((k_match, v_count) -> {
				counts[0] = counts[0] + v_count;
				if (!k_match) {
					counts[1] = counts[1] + v_count;
				}
			});
		});

		System.out.println("Total number of missclasification's :" + counts[1]
				+ " out of " + counts[0]);
	}

	private static String readAndPopulateProbTable(
			AlgoStructBean algoStructBean, Set<String> classProcessedSoFar) {

		boolean included = false;
		String recentTargetValue = null;
		AlgoInputBean algoInputBean = algoStructBean.getAlgoInptBean();
		AlgoOutputBean algoOutputBean = new AlgoOutputBean();
		NaiveModel naiveModel = algoOutputBean.getLearnedModel();
		naiveModel.setlSoothing(algoInputBean.getlSoothingFactor());

		OptimizedReaderBeans rdr = new OptimizedReaderBeans(
				algoInputBean.getDataUrl());
		String text = null;
		while ((text = rdr.readNextLine()) != null) {
			String[] splittedText = text.split(algoInputBean
					.getFiledSeparator());
			String targetClassValue = splittedText[algoInputBean
					.getTargetColumnIndex() - 1];

			if ((included && !recentTargetValue.equals(targetClassValue))
					|| classProcessedSoFar.contains(targetClassValue)) {
				targetClassValue = "0";
			} else {
				recentTargetValue = targetClassValue;
				included = true;
				targetClassValue = "1";
			}

			splittedText[algoInputBean.getTargetColumnIndex() - 1] = targetClassValue;

			for (int i = 0; i < splittedText.length; i++) {
				if (i + 1 == algoInputBean.getTargetColumnIndex()) {
					naiveModel.addToPriorProb(splittedText[i], 1);
					continue;
				}
				// populating tables
				naiveModel.addToTable(
						algoInputBean.getColumnNames().get(i + 1),
						algoInputBean.getTargetColumnName(), splittedText[i],
						splittedText[algoInputBean.getTargetColumnIndex() - 1],
						1, 0);
			}
		}

		if (recentTargetValue != null) {
			// normalize all probabilities
			naiveModel.normalizePriorProb();
			naiveModel.normalizeConditionProb();
			algoStructBean.setAlgoOutputBean(algoOutputBean);
			classProcessedSoFar.add(recentTargetValue);
		}

		return recentTargetValue;
	}

	public static void predictAccuracy(AlgoStructBean algoStructBean,
			String curTargetValue,
			Map<Integer, Map<Boolean, Integer>> totalTestAccuracyMatrix) {

		// read each record from a file and predict the class probabilities
		AlgoInputBean algoInptBean = algoStructBean.getAlgoInptBean();
		AlgoOutputBean algoOutputBean = algoStructBean.getAlgoOutputBean();
		int success = 0;
		int total = 0;
		Map<String, Map<String, Integer>> confusionMatrix = new TreeMap<>();

		OptimizedReaderBeans rdr = new OptimizedReaderBeans(
				algoInptBean.getTestDataUrl());
		String text = null;
		int testRecordIndex = 0;
		while ((text = rdr.readNextLine()) != null) {
			String[] splittedText = text
					.split(algoInptBean.getFiledSeparator());
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
			targetClassValue = targetClassValue.equals(curTargetValue) ? "1"
					: "0";
			if (predictedClass.equals(targetClassValue)) {
				success++;
			} else {
				/*
				 * System.out.println("Mismatched :: " + text + "[predicted =" +
				 * predictedClass + ", actual =" + targetClassValue + "]");
				 */
			}
			testRecordIndex++;
			updateTestAccuracyMatrix(testRecordIndex, totalTestAccuracyMatrix,
					predictedClass, targetClassValue);
			// updating the confusion matrix
			if (!confusionMatrix.containsKey(targetClassValue)) {
				confusionMatrix.put(targetClassValue,
						new TreeMap<String, Integer>());
			}
			if (!confusionMatrix.get(targetClassValue).containsKey(
					predictedClass)) {
				confusionMatrix.get(targetClassValue).put(predictedClass, 0);
			}
			int c = confusionMatrix.get(targetClassValue).get(predictedClass);
			confusionMatrix.get(targetClassValue).put(predictedClass, c + 1);

		}

		System.out.println("--------One-Vs-All-----------");
		System.out.println("START::");
		System.out.println("Actual Target Label Value :" + curTargetValue);
		ProbabilityUtil.printConfusionMatrix(total, success, confusionMatrix);
		System.out.println("END::");
	}

	private static void updateTestAccuracyMatrix(int testRecordIndex,
			Map<Integer, Map<Boolean, Integer>> totalTestAccuracyMatrix,
			String predictedClass, String targetClassValue) {
		if (!totalTestAccuracyMatrix.containsKey(testRecordIndex)) {
			totalTestAccuracyMatrix.put(testRecordIndex,
					new HashMap<Boolean, Integer>());
		}

		boolean matched = predictedClass.equals(targetClassValue);

		if (!totalTestAccuracyMatrix.get(testRecordIndex).containsKey(matched)) {
			totalTestAccuracyMatrix.get(testRecordIndex).put(matched, 0);
		}
		int count = totalTestAccuracyMatrix.get(testRecordIndex).get(matched);
		totalTestAccuracyMatrix.get(testRecordIndex).put(matched, count + 1);
	}
}
