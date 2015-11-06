/**
 * 
 */
package simplebeans;

import util.ProbabilityUtil;

/**
 * @author sumit
 *
 */
public class AlgoOutputBean {

	private NaiveModel modelData;

	/**
	 * 
	 */
	public AlgoOutputBean() {
		modelData = new NaiveModel();
	}

	public NaiveModel getLearnedModel() {
		return this.modelData;
	}

	public String getPrediction(QueryBean qb, String targetName) {
		return ProbabilityUtil.predictTargetClass(qb, modelData,
				modelData.getTargetClassValues(), targetName);
	}
}
