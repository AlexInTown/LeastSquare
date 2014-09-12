package com.yahoo.leastsquare;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class FeatureWeightDotProduct extends EvalFunc<Double>{

	@Override
	public Double exec(Tuple input) throws IOException {
		Tuple feature = (Tuple)input.get(0);
		Tuple weights = (Tuple)input.get(1);
		if(feature.size()+1 != weights.size())
			throw new IOException("Feature tuple dimmesion "+feature.size()+"+1 does not match weight dimmension " +weights.size()+ "!");
		double res = 0.0;
		Double val;
		for(int i = 0; i < feature.size(); ++i)
		{
			val = (Double) feature.get(i);
			if(val==null) val = 0.0;
			res += val * (Double) weights.get(i+1);
		}
		res += (Double)weights.get(0);
		return res;
	}


}
