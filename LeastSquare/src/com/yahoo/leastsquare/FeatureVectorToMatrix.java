package com.yahoo.leastsquare;

import java.io.IOException;

import org.apache.pig.Algebraic;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.TupleFactory;

import com.yahoo.leastsquare.MatrixUtils;
/**
 * Class for calculating XTX
 * @author zhenouyang
 *
 */
public class FeatureVectorToMatrix extends EvalFunc<DataBag> implements Algebraic{
	
    public String getInitial() {return Initial.class.getName();}
    public String getIntermed() {return Intermed.class.getName();}
    public String getFinal() {return Final.class.getName();}

    private static TupleFactory mTupleFactory = TupleFactory.getInstance();
    //private static BagFactory mBagFactory = BagFactory.getInstance();

    private static DataBag calculate(Tuple input) throws ExecException{
    	Object o = input.get(0);
    	if (o instanceof DataBag){
    		return calculateAndSumOver((DataBag) o);
    	}
    	if (o instanceof Tuple){
    		return calculateSingleTuple((Tuple) o);
    	}
    	return calculateSingleTuple(input);
    	
    }
    
    private static DataBag calculateSingleTuple(Tuple t) throws ExecException{
    	int size = t.size();
        	double[] vals = new double[size-1];
        	double weight = (Double) t.get(0);
        	Double val;
        	for(int i = 2; i < size; ++i){
        		val = (Double) t.get(i);
        		if(val==null) val = 0.0;
        		vals[i-1] = val;
        	}
        	vals[0] = 1;
        	
        	double[][] mat = new double[size-1][size-1];
        	for(int i = 0; i < size-1; ++i){
        		for(int j = i; j < size-1; ++j){
        			mat[i][j] = vals[i]*vals[j]*weight;
        			mat[j][i] = mat[i][j];
        		}
        	}
			return MatrixUtils.convertMatrixToBag(mat);        			
    }
    private static DataBag calculateAndSumOver(DataBag bag) throws ExecException{
    	int size = bag.iterator().next().size();
    	double[][] mat = new double[size-1][size-1];
    	double[] vals = new double[size-1];
    	Double val;
    	int i,j;
    	for(Tuple t : bag){
    		for(i = 2; i < size; ++i){
    			val = (Double) t.get(i);
    			if(val==null) val = 0.0;
        		vals[i-1] = val;
        	}
        	vals[0] = 1;
        	double weight = (Double) t.get(0);
        	for(i = 0; i < size-1; ++i){
        		for(j = i; j < size-1; ++j){
        			mat[i][j] += vals[i]*vals[j]*weight;
        		}
        	}
    	}
    	for(i = 0; i < size-1; ++i)
    	for(j = i; j < size-1; ++j)
    		mat[j][i] = mat[i][j];
    	return MatrixUtils.convertMatrixToBag(mat);
    }
    private static DataBag sumByElement(DataBag bag) throws IOException{
    	double[][] res = null;
    	for(Tuple tu: bag){
    		DataBag ba = (DataBag) tu.get(0);
    		if(res==null){
    			int size = (int)ba.size();
    			res = new double[size][size];
    		}
    		MatrixUtils.addMatrix(res, MatrixUtils.convertBagToMatrix(ba));
    	}
    	return MatrixUtils.convertMatrixToBag(res);
    }
    
    static public class Initial extends EvalFunc<Tuple> {
        public Tuple exec(Tuple input) throws IOException {
        	return mTupleFactory.newTuple(calculate(input));
        }
    }


    static public class Intermed extends EvalFunc<Tuple> {
        public Tuple exec(Tuple input) throws IOException{
        	DataBag bag = (DataBag) input.get(0);
        	return mTupleFactory.newTuple(sumByElement(bag));
        }
    }

    static public class Final extends EvalFunc<DataBag> {
        public DataBag exec(Tuple input) throws IOException {
        	DataBag bag = (DataBag) input.get(0);
        	return sumByElement(bag);
        }
    }

	@Override
	public DataBag exec(Tuple input) throws IOException {
		return calculate(input);
	}
	

}
