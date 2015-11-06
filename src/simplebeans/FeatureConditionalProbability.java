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

public class FeatureConditionalProbability {

	private String featureName;
	private String targetName;

	// feature value, class-value, conditional probability
	private Map<String, Map<String, Fractions>> table;

	FeatureConditionalProbability(String pFeatureName, String pTargetName) {
		this.featureName = pFeatureName;
		this.targetName = pTargetName;
		this.table = new HashMap<String, Map<String, Fractions>>();
	}

	public String getFeatureName() {
		return featureName;
	}

	public boolean isFeatureValueContains(String featureValue) {
		return this.table.containsKey(featureValue);
	}

	public boolean isFeatureAndClassValuesContains(String featureValue,
			String targetValue) {
		if (this.isFeatureValueContains(featureValue)) {
			return this.table.get(featureValue).containsKey(targetValue);
		}
		return false;
	}

	public void incrementCondProbabilty(String featureValue,
			String targetValue, long fetaureCount, long targetCount) {
		if (!this.isFeatureAndClassValuesContains(featureValue, targetValue)) {
			// add a new featre mapping
			if (!this.isFeatureValueContains(featureValue)) {
				this.table.put(featureValue, new HashMap<String, Fractions>());
			}
			this.table.get(featureValue).put(targetValue,
					new Fractions(fetaureCount,targetCount));
			return;
		}

		Fractions f = this.table.get(featureValue).get(targetValue);
		f.setNumerator(f.getNumerator() + fetaureCount);
		f.setDenominator(f.getDenominator() + targetCount);

	}

	public String getTargetName() {
		return targetName;
	}

	public int getTotalFeatureClassCount() {
		return this.table.size();
	}

	public Fractions geProbabilty(String featureValue, String targetValue) {
		if (!this.isFeatureAndClassValuesContains(featureValue, targetValue)) {
			return null;
		}
		return this.table.get(featureValue).get(targetValue);
	}

	public void normalizeCondProb(String targetValue, long targetCount) {
		for (Map<String, Fractions> x : this.table.values()) {
			for (Map.Entry<String, Fractions> y : x.entrySet()) {
				if (targetValue.equals(y.getKey())) {
					y.getValue().setDenominator(targetCount);
				}
			}
		}
	}

	public String toString() {
		return "\nColumn_name :" + this.featureName + ", \n targetName:"
				+ this.targetName + ", \n Table Distributions" + this.table+"\n";
	}
}
