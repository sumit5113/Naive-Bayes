package simplebeans;

public class Fractions implements Cloneable{
	private long numerator;
	private long denominator;

	public Fractions(long pNumerator, long pDenominator) {
		this.denominator = pDenominator;
		this.numerator = pNumerator;
	}
	
	@Override
	public Fractions clone(){
		return new Fractions(this.numerator,this.denominator);
	}

	@Override 
	public String toString(){
		return this.numerator+"/"+this.denominator;
	}
	
	public long getNumerator() {
		return numerator;
	}

	public void setNumerator(long numerator) {
		this.numerator = numerator;
	}

	public long getDenominator() {
		return denominator;
	}

	public void setDenominator(long denominator) {
		this.denominator = denominator;
	}
}
