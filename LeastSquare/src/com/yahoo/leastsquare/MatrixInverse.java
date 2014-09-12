package com.yahoo.leastsquare;

import java.io.IOException;
import Jama.*;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
/**
 * Class for calculating (XTX)-1
 * @author zhenouyang
 *
 */
public class MatrixInverse extends EvalFunc<DataBag> {

	//private static TupleFactory mTupleFactory = TupleFactory.getInstance();
	@Override
	public DataBag exec(Tuple input) throws IOException {
		DataBag bag = (DataBag) input.get(0);
		Matrix m = new Matrix(MatrixUtils.convertBagToMatrix(bag));
		Matrix res = m.inverse();
		DataBag resBag = MatrixUtils.convertMatrixToBag(res.getArray());
		//return mTupleFactory.newTuple(resBag);
		return resBag;
	}
}
