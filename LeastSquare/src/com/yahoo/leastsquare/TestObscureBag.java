package com.yahoo.leastsquare;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
/**
 * Test class for obscure DataBag in pig script
 * @author zhenouyang
 *
 */
public class TestObscureBag extends EvalFunc<DataBag>{

	@Override
	public DataBag exec(Tuple input) throws IOException {
		input.append(0.0);
		int n = 3;
		double[][] mat = new double[n][n];
		for(int i = 0; i < n; ++i)
			for(int j = 0; j < n; ++j)
				mat[i][j] = (double) i+j;
		return MatrixUtils.convertMatrixToBag(mat);
	}
}
