package computedparameters;

public class JoshComputedParamsBuilder {

	private static final String			FORMULA					= "formula";
	private static final String			ACCEPT_NEGATIVE_VALUE	= "acceptNegativeValues";
	private static final String			S1						= "S1";
	private static final String			S2						= "S2";

	private String				formula;
	private boolean				acceptNegativeValues;
	private String				s1;
	private String				s2;

	public JoshComputedParamsBuilder(String s1) {
		this.s1 = s1;
	}

	public JoshComputedParamsBuilder s2(String s2) {
		this.s2 = s2;
		return this;
	}

	public JoshComputedParamsBuilder formula(String formula) {
		this.formula = formula;
		return this;
	}

	public JoshComputedParamsBuilder acceptNeg(Boolean acceptNegativeValues) {
		this.acceptNegativeValues = acceptNegativeValues;
		return this;
	}

	public ComputedSeriesParameters build() {
		// controle des parametres si besoin

		// 
		ComputedSeriesParameters params = new ComputedSeriesParameters();
		params.addLongAndShortParam(S1, s1);
		params.addLongAndShortParam(S2, s2);
		params.addLongAndShortParam(FORMULA, formula);
		params.addLongOnlyParam(ACCEPT_NEGATIVE_VALUE, "" + acceptNegativeValues);
		return params;
	}

}
