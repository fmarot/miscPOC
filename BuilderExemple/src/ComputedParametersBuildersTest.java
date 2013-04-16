import org.junit.Test;

import computedparameters.ComputedSeriesParameters;
import computedparameters.JoshComputedParamsBuilder;
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
				.build();
		System.out.println("Subtract result long = " + result.buildLong());
		System.out.println("Subtract result short = " + result.buildShort());

		ComputedSeriesParameters perfResult = PerfusionComputedSeriesParameters.create()
				.acceptNeg("toto")
				.CSFFilterMax("titi")
				.endOfSignalIndex("end")
				.formula("S2-S3-S4/S5")
				.build();
		System.out.println("Perf result long = " + perfResult.buildLong());
		System.out.println("Perf result short = " + perfResult.buildShort());

		ComputedSeriesParameters resultJosh = new JoshComputedParamsBuilder("NCCT # 02/03/2013")
				.s2("T0 # 18/05/2013")
				.formula("S2-S1")
				.acceptNeg(true)
				.build();
		System.out.println("Josh result long = " + resultJosh.buildLong());
		System.out.println("Josh result short = " + resultJosh.buildShort());
	}

}
