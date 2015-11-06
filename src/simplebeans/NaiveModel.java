package simplebeans;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NaiveModel {
	private Map<String, FeatureConditionalProbability> table;
	private Map<String, Fractions> priorProbabilities;
	private int lSoothing;
	private int totalNumberOfRows = 0;
	
	public NaiveModel() {
		this.table = new HashMap<String, FeatureConditionalProbability>();
		this.priorProbabilities = new HashMap<String, Fractions>();
	}

	private boolean isConatinsInTable(String featureName, String targetName) {
		return this.table.containsKey(getKey(featureName, targetName));
	}

	private String getKey(String featureName, String targetName) {
		// TODO Auto-generated method stub
		return featureName + "," + targetName;
	}

	public void addToTable(String featureName, String targetName,
			String featureValue, String targetValue, int featureCount,
			int targetCount) {

		// check whether it is der or not in the table.
		if (!isConatinsInTable(featureName, targetName)) {
			this.table.put(getKey(featureName, targetName),
					new FeatureConditionalProbability(featureName, targetName));
		}
		FeatureConditionalProbability f = this.getCondProbability(featureName,
				targetName);
		// now since this is a new type value
		//if (!f.isFeatureAndClassValuesContains(featureValue, targetValue)) {
			f.incrementCondProbabilty(featureValue, targetValue, featureCount,
					targetCount);
		//}
	}

	public FeatureConditionalProbability getCondProbability(String featureName,
			String targetName) {
		return this.table.get(getKey(featureName, targetName));
	}

	public int getlSoothing() {
		return lSoothing;
	}

	public void setlSoothing(int lSoothing) {
		this.lSoothing = lSoothing;
	}

	public Fractions getTargetClassProb(String targetValue) {
		return this.priorProbabilities.get(targetValue);
	}

	public int getTaregtClassCount() {
		return this.priorProbabilities.size();
	}
	
	public void addToPriorProb(String targetValue,long count){
		this.totalNumberOfRows++;
		if(!this.priorProbabilities.containsKey(targetValue)){
			this.priorProbabilities.put(targetValue, new Fractions(1, 0));
			return;
		}
		Fractions f = this.priorProbabilities.get(targetValue);
		f.setNumerator(f.getNumerator()+count);
	}
	
	public void normalizePriorProb(){
		for(Map.Entry<String, Fractions> f:this.priorProbabilities.entrySet()){
			f.getValue().setDenominator(totalNumberOfRows);
		}
	}
	
	public void normalizeConditionProb(){
		for(Map.Entry<String, Fractions> f:this.priorProbabilities.entrySet()){
			for(Map.Entry<String, FeatureConditionalProbability> cp:this.table.entrySet()){
				cp.getValue().normalizeCondProb(f.getKey(), f.getValue().getNumerator());
			}
		}
	}
	
	public Set<String> getTargetClassValues(){
		return this.priorProbabilities.keySet();
	}

	public Map<String, FeatureConditionalProbability> getTable() {
		return table;
	}

	public void setTable(Map<String, FeatureConditionalProbability> table) {
		this.table = table;
	}

	public Map<String, Fractions> getPriorProbabilities() {
		return priorProbabilities;
	}
}