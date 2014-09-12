package com.yahoo.leastsquare;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.apache.pig.data.DataBag;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 3;
		double[][] mat = new double[n][n];
		for(int i = 0; i < n; ++i)
			for(int j = 0; j < n; ++j)
				mat[i][j] = (double) i+j;

		
		byte[] bytes = new byte[8*n];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		for(int i = 0; i < n; ++i){
			//System.out.println(Arrays.asList(mat[i])[0]);
			buffer.putDouble(mat[0][i]);
		}
		System.out.println(bytes);
		System.out.println(mat[0] instanceof double[]);
		List list = Arrays.asList(mat[1]);
		System.out.println(list.get(0));
		double[] arr = new double[23];
		List list2 = Arrays.asList(arr);
		System.out.println(list2.get(3));

	}

}
