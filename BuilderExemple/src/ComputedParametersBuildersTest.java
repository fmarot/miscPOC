import org.junit.Test;

import computedparameters.ComputedSeriesParameters;
import computedparameters.PerfusionComputedSeriesParameters;
import computedparameters.SubtractComputedParamsBuilder;



public class ComputedParametersBuildersTest {

	@Test
	public void test() {
		ComputedSeriesParameters result = SubtractComputedParamsBuilder.create()
				.s1("NCCT # 02/03/2013")
				.s2("T0 # 18/05/2013")
				.formula("S2-S1")
				.acceptNeg(true)
				.toResult();
		System.out.println("Subtract result long = " + result.buildLong());
		System.out.println("Subtract result short = " + result.buildShort());

		ComputedSeriesParameters perfResult = PerfusionComputedSeriesParameters.create()
				.acceptNeg("toto")
				.CSFFilterMax("titi")
				.endOfSignalIndex("end")
				.formula("S2-S3-S4/S5")
				.toResult();
		System.out.println("Perf result long = " + perfResult.buildLong());
		System.out.println("Perf result short = " + perfResult.buildShort());
	}

}
