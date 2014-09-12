package com.yahoo.leastsquare;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import Jama.Matrix;
/**
 * Calculate the weights of least square linear regression using the intermediate value of 
 * (XTX)-1 and XTy. 
 * We just have to multiply these two matrix then we get each weight (and bias).
 * @author zhenouyang
 */
public class LeastSquare extends EvalFunc<Tuple>{

	private static TupleFactory mTupleFactory = TupleFactory.getInstance();
	private static BagFactory mBagFactory = BagFactory.getInstance();

	/**
	 * Calculate the weights of least square linear regression using the intermediate value of 
	 * (XTX)-1 and XTy. See corresponding pig script 'least_square.pig' for further information.
	 * @param input tuple including matrix bag and vector tuple. 
	 * @return bag of weights, each tuple of the bag wrap a single double valued weight (the last one is the bias).
	 */
	@Override
	public Tuple exec(Tuple input) throws IOException {
		// Get the parameters matrix (XTX)-1 and vector XTy
		DataBag bag = (DataBag) input.get(0);
		Tuple t = (Tuple) input.get(1);
		
		// Testing the dimension (plus one is because vector does including the bias attribute).
		if(bag.size()!= t.size()){
			throw new IOException("(XTX)-1 XTy matrix dimension does not match!");
		}
		// Get the matrix dimension and retrieve vector from tuple
		int size = (int)bag.size();
		double[] vec = new double[size];
		for(int i = 0; i < size; ++i)
			vec[i] = (Double) t.get(i);
		
		// Matrix multiplication
		Matrix mat1 = new Matrix(MatrixUtils.convertBagToMatrix(bag));
		Matrix mat2 = new Matrix(vec, 1);
		Matrix res = mat2.times(mat1);
		
		// Wrapping output weights into a bag
		//DataBag outBag = mBagFactory.newDefaultBag();
		//for(int i = 0; i < size; ++i){
		//	outBag.add(mTupleFactory.newTuple(res.get(0, i)));
		//}
		//return mTupleFactory.newTuple(outBag);
		//return outBag;
		
		// Wrapping output weights into a tuple.
		Tuple outTuple = mTupleFactory.newTuple();
		for(int i = 0; i < size; ++i){
			outTuple.append(res.get(0, i));
		}
		return outTuple;
	}
}
