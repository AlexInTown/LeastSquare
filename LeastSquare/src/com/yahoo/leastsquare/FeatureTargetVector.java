package com.yahoo.leastsquare;

import java.io.IOException;

import org.apache.pig.Algebraic;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Compute XTy
 * @author zhenouyang
 *
 */
public class FeatureTargetVector extends EvalFunc<Tuple> implements Algebraic{

	private static TupleFactory mTupleFactory = TupleFactory.getInstance();

	public String getInitial() {return Initial.class.getName();}
    public String getIntermed() {return Intermed.class.getName();}
    public String getFinal() {return Final.class.getName();}

    private static Tuple sumByElement(DataBag bag) throws ExecException{
    	Tuple result = mTupleFactory.newTuple();
    	int size = bag.iterator().next().size();
    	int i;
    	for(i = 0; i < size; ++i)
    		result.append(0.0);
    	for(Tuple t: bag){
    		for(i = 0; i < size; ++i)
    			result.set(i, (Double) result.get(i) + (Double)t.get(i));
    	}
    	return result;
    }
    
    private static Tuple calculateTargetAndSum(DataBag bag) throws ExecException{
    	int size = bag.iterator().next().size();
    	Tuple result = mTupleFactory.newTuple();
    	int i;
    	for(i = 0; i < size-1; ++i)
    		result.append(0.0);
    	double target;
    	double weight;
    	Double val;
    	for(Tuple t: bag){
    		weight = (Double)t.get(0);
    		target = (Double)t.get(1);
    		result.set(0, (Double) result.get(0) +target*weight);
    		for(i = 2; i < size; ++i){
    			val = (Double) t.get(i);
    			if(val==null) val = 0.0;
    			result.set(i-1, (Double) result.get(i-1) + target* val * weight);
    		}
    	}
    	return result;
    	
    }
    
    private static Tuple calculateTarget(Tuple input) throws ExecException {

    	double weight = (Double) input.get(0);
    	double target = (Double) input.get(1);
    	int size = input.size();
    	Tuple output = mTupleFactory.newTuple();
    	output.append(target * weight);
    	Double val;
    	for(int i = 2; i < size; ++i){
    		val = (Double) input.get(i);
    		if(val==null) val = 0.0;
    		output.append(val* target * weight);
    	}
    	return output;
    }

    private static Tuple calculate(Tuple input) throws ExecException{
    	Object o = input.get(0);
    	if(o instanceof DataBag){
    		return calculateTargetAndSum((DataBag) o);
    	}
    	if (o instanceof Tuple){
    		return calculateTarget((Tuple) o);
    	}
    	return calculateTarget(input);
    }
    
    
    public static class Initial extends EvalFunc<Tuple>{

		@Override
		public Tuple exec(Tuple input) throws IOException {
			return calculate(input);
		}
    	
    }
    public static class Intermed extends EvalFunc<Tuple>{

		@Override
		public Tuple exec(Tuple input) throws IOException {
			DataBag bag = (DataBag) input.get(0);
			return sumByElement(bag);
		}
    	
    }
    public static class Final extends EvalFunc<Tuple>{
    	@Override
		public Tuple exec(Tuple input) throws IOException {
    		DataBag bag =(DataBag) input.get(0);
			return sumByElement(bag);
		}
    	
    }

	@Override
	public Tuple exec(Tuple input) throws IOException {
		return calculate(input);
	}
}
