/**
 * 
 */
package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import simplebeans.FeatureConditionalProbability;
import simplebeans.Fractions;
import simplebeans.NaiveModel;
import simplebeans.QueryBean;

/**
 * @author sumit
 *
 */
public class ProbabilityUtil {

	public static Fractions getLaplcaeSmoothing(
			FeatureConditionalProbability featureProbs, String featureValue,
			String targetValue, int lSmoothing, Fractions targetClassProb) {
		Fractions f = featureProbs.geProbabilty(featureValue, targetValue);
		if (f == null) {
			f = new Fractions(0, targetClassProb.getNumerator());
		}

		return laplaceSmoothing(f, lSmoothing,
				featureProbs.getTotalFeatureClassCount());
		// now add the lSmoothing and kSmoothing to numerator and denominator

	}

	private static Fractions laplaceSmoothing(Fractions f, int lSmoothing,
			int kSoothing) {
		f = f.clone();

		f.setNumerator(f.getNumerator() + lSmoothing);
		f.setDenominator(f.getDenominator() + lSmoothing * kSoothing);
		return f;
	}

	public static String predictTargetClass(QueryBean qb, NaiveModel modelData,
			Set<String> targetValues, String targetName) {
		String classPredicted = "";
		double maxOddsRatio = 0.0;
		Map<String, Double> allPredictions = new HashMap<>();
		// calculating => prob(class=y|training instance)
		for (String targetValue : targetValues) {
			double classProb = 1;
			for (Map.Entry<String, String> q : qb.getQuery().entrySet()) {
				FeatureConditionalProbability fcp = modelData
						.getCondProbability(q.getKey(), targetName);
				Fractions f = getLaplcaeSmoothing(fcp, q.getValue(),
						targetValue, modelData.getlSoothing(),
						modelData.getTargetClassProb(targetValue));
				classProb = classProb * (1.0 * f.getNumerator())
						/ f.getDenominator();
			}
			// multiply with the class probability
			Fractions ftp = laplaceSmoothing(
					modelData.getTargetClassProb(targetValue),
					modelData.getlSoothing(), modelData.getTaregtClassCount());
			classProb = classProb * (1.0 * ftp.getNumerator())
					/ ftp.getDenominator();
			allPredictions.put(targetValue, classProb);
			// mle estimation, maximum class predictions
			if (classProb > maxOddsRatio) {
				maxOddsRatio = classProb;
				classPredicted = targetValue;
			}
		}
		// System.out.println("all prob:"+allPredictions);
		return classPredicted;
	}

	public static void printConfusionMatrix(int total, int success,
			Map<String, Map<String, Integer>> confusionMatrix) {
		System.out.println("===========Statistics================");
		System.out.println("Total number of records tested : " + total);
		System.out.println("Total number of records successfully predicted : "
				+ success);
		System.out.println("Accuracy:: " + (100.0) * success / total);
		
		int noClmns = confusionMatrix.size();
		// print columns
		for (int i = 0; i < noClmns; i++) {
			System.out.print((char) ('a' + i) + "\t");
		}
		System.out.println("<-- confusion-matrix");
		char j = 'a';
		String[] targetClassValues = confusionMatrix.keySet().toArray(
				new String[0]);
		for (String tc : targetClassValues) {
			Map<String, Integer> y = confusionMatrix.get(tc);
			for (String t : targetClassValues) {
				System.out.print((y.containsKey(t) ? y.get(t) : 0) + "\t");
			}
			System.out.println(" | " + j + " = " + tc);
			j++;
		}
	}

}
