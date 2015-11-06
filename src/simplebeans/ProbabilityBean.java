/**
 * 
 */
package simplebeans;

/**
 * @author sumit
 *
 */
public class ProbabilityBean {
	private String givenName;
	private Fractions probability;
	private String givenNameValue;

	/**
	 * 
	 */
	public ProbabilityBean() {
		// TODO Auto-generated constructor stub
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public Fractions getProbability() {
		return this.probability;
	}

	public String getGivenNameValue() {
		return givenNameValue;
	}

	public void setGivenNameValue(String givenNameValue) {
		this.givenNameValue = givenNameValue;
	}

	public void incrementProbabilityCount(ProbabilityBean probBean) {
		Fractions probUpdtr = probBean.getProbability();
		this.probability.setNumerator(this.probability.getNumerator()
				+ probUpdtr.getNumerator());
		this.probability.setDenominator(this.probability.getDenominator()
				+ probUpdtr.getDenominator());

	}

	public void incrementProbabilityCount(long numerator, long denominator) {
		this.probability.setNumerator(this.probability.getNumerator()
				+ numerator);
		this.probability.setDenominator(this.probability.getDenominator()
				+ denominator);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ProbabilityBean) {
			ProbabilityBean other = (ProbabilityBean) o;
			return other != null
					&& ((this.givenName == null && other.givenName == null) || (this.givenName != null && this.givenName
							.equals(other.givenName)))
					&& ((this.givenNameValue == null && other.givenNameValue == null) || (this.givenNameValue != null && this.givenNameValue
							.equals(other.givenNameValue)));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return ("" + this.givenName + "," + this.givenNameValue).hashCode();
	}

	@Override
	public String toString() {
		return "{ P(" + this.givenName + " = " + this.givenNameValue + ") = "
				+ this.probability + "}";
	}
}
