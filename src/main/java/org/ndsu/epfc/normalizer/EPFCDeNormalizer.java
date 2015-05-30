package org.ndsu.epfc.normalizer;
/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.text.DecimalFormat;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.norm.Normalizer;

/**
 * MaxMin normalization method, which normalize data in regard to min and max elements in training set (by columns)
 * Normalization is done according to formula:
 * normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i])
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class EPFCDeNormalizer implements Normalizer {
    /**
	 * @uml.property  name="maxIp" multiplicity="(0 -1)" dimension="1"
	 */
    double[] maxIp; // contains max values for all columns
    /**
	 * @uml.property  name="minIp" multiplicity="(0 -1)" dimension="1"
	 */
    double[] minIp; // contains min values for all columns
    
    /**
	 * @uml.property  name="maxOp" multiplicity="(0 -1)" dimension="1"
	 */
    double[] maxOp = new double[]{20098.65,19501.22,18772.32,18242.07,18194.23,18771.99,20263.31,21973.48,23529.41,25023.34,26434.02,27328.09,27665.7,27727.11,27762.02,27705.18,27513.16,27181.97,26570.76,25622.22,25196.29,24589.23,22976.6,21675.17}; // contains max values for all columns
    /**
	 * @uml.property  name="minOp" multiplicity="(0 -1)" dimension="1"
	 */
    double[] minOp =new double[]{9013.52,8883.19,8716.4,8670.9,8664.59,9165.42,9588.67,10022.2,10649.5,10930,10871.62,11007.82,11077.8,11089.54,11065.41,11059.43,11427.62,11636.25,11813.68,11992,11357.3,10639.18,10028.58,9242.75}; // contains min values for all columns
    
    static DecimalFormat twoDForm = new DecimalFormat("#.##");

    /*public static final double MIN_RANGE = 0;
    public static final double MAX_RANGE = 1;
    
    static double maxip;
    static double minip;
    static double maxop = Double.MIN_VALUE;
    static double minop = Double.MAX_VALUE;*/
    
   
    public void normalize(DataSet dataSet) {
        // find min i max vectors
        findMaxinAndMinVectors(dataSet);
        findMaxinAndMinOpVectors(dataSet);
       
        // izvrsi normalizciju / deljenje
        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            double[] output = dataSetRow.getDesiredOutput();
            
            //findMinMax(output,minop,maxop);
            
            double[] normalizedInput = normalizeMaxMin(input);
            
            if(output != null){
            	double[] normalizedOutput = normalizeMaxMinOP(output);
            	dataSetRow.setDesiredOutput(normalizedOutput);
            }
            dataSetRow.setInput(normalizedInput);
            
        }

    }
    
    
    public  double [] deNormalizeOp(double[] op) {
    	
    	   int size = op.length;
    	   
    	   double [] dnormop = new double [size];
    	  
            for (int i = 0; i < size; i++) {
            	if(minOp !=null && maxOp!=null){
            		dnormop[i] = Double.valueOf(twoDForm.format(denormalize(op[i], minOp[i], maxOp[i])));
            	}else{
            		dnormop[i] = Double.valueOf(twoDForm.format(denormalize(op[i], minIp[i], maxIp[i])));
            	}
			}
            
           return dnormop;
        

    }
    
    public  double [] deNormalizeIp(double[] ip) {
    	
 	   int size = ip.length;
 	   
 	   double [] dnormop = new double [size];
 	  
         for (int i = 0; i < size; i++) {
         	dnormop[i] = denormalize(ip[i], minIp[i], maxIp[i]);
			}
         
        return dnormop;
     

 }
    
    
   /* public void findMinMax(double[] op, double min, double max){
    	
    	if(op == null) return;
    	
    	for (double d : op) {    		

    		if(d < min){
				minop = d;
			}
			
			if(d > max){
				maxop = d;
			}
		}
    	
    	
    	
    }*/
    
    
    
    
    private void findMaxinAndMinVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        maxIp = new double[inputSize];
        minIp = new double[inputSize];

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (Math.abs(input[i]) > maxIp[i] || maxIp[i] == 0) {
                    maxIp[i] = Math.abs(input[i]);
                }
                if (Math.abs(input[i]) < minIp[i] || minIp[i] == 0) {
                    minIp[i] = Math.abs(input[i]);
                }
            }
        }        
        
        for (int i = 0; i < inputSize; i++) {           
            if (minIp[i] == maxIp[i] ) {
                minIp[i] = 0;
            }
        }
    }
    
    private void findMaxinAndMinOpVectors(DataSet dataSet) {
        int inputSize = dataSet.getOutputSize();
        if(inputSize == 0) return;
        maxOp = new double[inputSize];
        minOp = new double[inputSize];

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < inputSize; i++) {
                if (Math.abs(output[i]) > maxOp[i]) {
                    maxOp[i] = Math.abs(output[i]);
                }
                if (Math.abs(output[i]) < minOp[i] || minOp[i] == 0) {
                    minOp[i] = Math.abs(output[i]);
                }
            }
        }        
    }

    private double[] normalizeMaxMin(double[] vector) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
        	
        	//Custom logic for year month and day
        	if(i==0){
        		minIp[i] = 2009;
        		maxIp[i] = 2015;
        	}
        	
        	if(i==1){
        		minIp[i] = 1;
        		maxIp[i] = 12;
        	}
                normalizedVector[i] = normalize(vector[i], minIp[i], maxIp[i]);
        	
        }

        return normalizedVector;
    }
    
    private double[] normalizeMaxMinOP(double[] vector) {
    	
    	
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
        		normalizedVector[i] = normalize(vector[i], minOp[i], maxOp[i]);
        	
        }

        return normalizedVector;
    }
    
    
    
    
    static public double normalize(double val, double min, double max, 
            double minRange, double maxRange){
       return ((val - min) / (max - min)) ;//* (maxRange - minRange) + minRange;
    }

    /**
     * Normalizza un valore tra zero e uno
     * @param val valore da normalizzare
     * @param min minimo
     * @param max massimo
     * @return valore normalizzato
     */
    static public double normalize(double val, double min, double max){
       return normalize(val, min, max, 0, 1);
    }
    
    /**
     * Denormalizza un valore
     * @param val valore da denormalizzare
     * @param min minimo
     * @param max massimo
     * @param minRange valore minimo del range normalizzato
     * @param maxRange valore massimo del range normalizzato
     * @return valore denormalizzato
     */
     public static double denormalize(double val, double min, double max,
            double minRange, double maxRange){
    	 return (val* (max - min))  + min;
       //return (val - minRange) / (maxRange - minRange) * (max - min) + min;
    }

    /**
     * Deormalizza un valore tra zero e uno
     * @param val valore da denormalizzare
     * @param min minimo
     * @param max massimo
     * @return valore denormalizzato
     */
     public static double denormalize(double val, double min, double max){
       return denormalize(val, min, max, 0, 1);
    }
}
