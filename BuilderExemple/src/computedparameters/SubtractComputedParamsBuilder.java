package computedparameters;

public class SubtractComputedParamsBuilder {

	private static final String			FORMULA					= "formula";
	private static final String			ACCEPT_NEGATIVE_VALUE	= "acceptNegativeValues";
	private static final String			S1						= "S1";
	private static final String			S2						= "S2";

	private ComputedSeriesParameters	params;

	private SubtractComputedParamsBuilder() {
		params = new ComputedSeriesParameters();
	}

	public static SubtractComputedParamsBuilder create() {
		return new SubtractComputedParamsBuilder();
	}

	public SubtractComputedParamsBuilder s1(String s1) {
		params.addLongAndShortParam(S1, s1);
		return this;
	}

	public SubtractComputedParamsBuilder s2(String s2) {
		params.addLongAndShortParam(S2, s2);
		return this;
	}

	public SubtractComputedParamsBuilder formula(String formula) {
		params.addLongAndShortParam(FORMULA, formula);
		return this;
	}

	public SubtractComputedParamsBuilder acceptNeg(Boolean bool) {
		params.addLongOnlyParam(ACCEPT_NEGATIVE_VALUE, bool.toString());
		return this;
	}

	public ComputedSeriesParameters toResult() {
		return params;
	}

}
