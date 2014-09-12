package com.yahoo.leastsquare;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.backend.hadoop.hbase.HBaseBinaryConverter;
import org.apache.pig.data.DataByteArray;

public class MatrixUtils {
    private static TupleFactory mTupleFactory = TupleFactory.getInstance();
    private static BagFactory mBagFactory = BagFactory.getInstance();

    public static double[][] convertBagToMatrix(DataBag bag) throws IOException{
    	//DataBag bag = (DataBag) t.get(0);
    	int size = (int) bag.size();
    	double[][] mat = new double[size][size];
    	int i = 0;
    	for(Tuple tu : bag){
    		//byte[] bytes = (byte[]) tu.get(0);
    		//double[] row = convertByteArrayToDoubleArray(bytes);
    		//double[] row = convertTupleToDoubleArray(tu);
    		//mat[i] = row;
    		fillDoubleArrayWithTuple(mat[i], tu);
    		++i;
    	}
    	return mat;
    }
    public static void fillDoubleArrayWithTuple(double[] arr, Tuple t) throws ExecException{
    	for(int i = 0; i < arr.length; ++i)
    		arr[i] = (Double)t.get(i);
    }
    
    public static double[] convertTupleToDoubleArray(Tuple t) throws ExecException{
    	int size = t.size();
    	double[] vals = new double[size];
    	for(int i = 0; i < size; ++i)
    		vals[i] = (Double) t.get(i);
    	return vals;
    }

    public static Tuple convertDoubleArrayToTuple(double[] vals){
    	Tuple res = mTupleFactory.newTuple();
    	for(int i = 0; i < vals.length; ++i)
    		res.append(vals[i]);
    	return res;
    }

    private static double[] convertByteArrayToDoubleArray(byte[] bytes)
    {
    	double[] res = new double[bytes.length/8];
    	ByteBuffer buffer = ByteBuffer.wrap(bytes);
    	for(int i = 0; i < res.length; ++i)
    		res[i] = buffer.getDouble();
    	return res;
    }

    private static byte[] convertDoubleArrayToByteArray(double[] doubles)
    {
    	byte[] res = new byte[doubles.length*8];
    	ByteBuffer buffer = ByteBuffer.wrap(res);
    	for(int i = 0; i < doubles.length; ++i)
    		buffer.putDouble(doubles[i]);
    	return res;
    }
    public static byte[] convertFeatureTupleToByteArray(Tuple tuple) throws ExecException{
    	int size = tuple.size()+1;
    	byte[] res = new byte[size*8];
    	ByteBuffer buffer = ByteBuffer.wrap(res);
    	for(int i = 0; i < size-1; ++i)
    		buffer.putDouble((Double) tuple.get(i));
    	buffer.putDouble(1.0);
    	return res;
    }
    public static DataBag convertMatrixToBag(double[][] mat){
    	if (mat==null)
    		return null;
    	DataBag bag = mBagFactory.newDefaultBag();
    	for(int i = 0; i < mat.length; ++i){
    		//bag.add(mTupleFactory.newTuple(convertDoubleArrayToByteArray(mat[i]))); 
    		bag.add(convertDoubleArrayToTuple(mat[i]));
    	}
    	return bag;
    }
    public static void addMatrix(double[][] a, double[][] b){
    	int s1 = a.length;
    	if(s1==0) return;
    	int s2 = a[0].length;
    	for(int i = 0; i < s1; ++i)
    		for(int j = 0; j < s2; ++j)
    			a[i][j] += b[i][j];
    }
    public static void addVector(double[] a, double[] b)
    {
    	for(int i = 0; i < a.length; ++i)
    		a[i]+=b[i];
    }

   
 

}
