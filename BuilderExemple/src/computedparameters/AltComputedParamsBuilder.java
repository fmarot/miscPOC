package computedparameters;

public class AltComputedParamsBuilder {


	private ComputedSeriesParameters	params;

	private AltComputedParamsBuilder(String formula, Boolean acceptNegativValues) {
		params = new ComputedSeriesParameters();
		params.addLongAndShortParam("formula", formula);
		params.addLongOnlyParam("acceptNegativValues", acceptNegativValues.toString());
	}

	// with mandatory params
	public static AltComputedParamsBuilder create(String formula, Boolean acceptNegativValues) {
		return new AltComputedParamsBuilder(formula, acceptNegativValues);
	}

	public AltComputedParamsBuilder s1(String s1) {
		params.addLongAndShortParam("S1", s1);
		return this;
	}

	public AltComputedParamsBuilder s2(String s2) {
		params.addLongOnlyParam("S2", s2);
		return this;
	}


	public ComputedSeriesParameters toResult() {
		return params;
	}

}
