package org.ndsu.epfc.normalizer;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TrainingSetImport;


public class RunNormalizer {
	
	/**
	 * @uml.property  name="normalizer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	EPFCNormalizer normalizer = new EPFCNormalizer();
	
    
    public void normalize(String ipFilePath, int inputCount, int outputCount, String opFilePath) throws NumberFormatException, FileNotFoundException, IOException{
    	 
        
        DataSet trainSet =  TrainingSetImport.importFromFile(ipFilePath, inputCount, outputCount, ",");
        //trainSet.normalize(normalizer);
        
        
        File newTextFile = new File(opFilePath);
        FileWriter fileWriter = new FileWriter(newTextFile,true);
        BufferedWriter bw = new BufferedWriter(fileWriter);

        
        StringBuilder b;
        for(DataSetRow trainSetRow : trainSet.getRows()) {
        	b = new StringBuilder();
        	double [] ip = trainSetRow.getInput();
        	double [] op = trainSetRow.getDesiredOutput();
        	for (double d : ip) {
				b.append(d)
				  .append(",");
			}
        	
        	if(outputCount != 0){
	        	
	        	for (double d : op) {
	        		b.append(d)
					  .append(",");
				}
        	
        	}
        	//b.replace(b.length()-1, b.length()-1, "\n");
        	String s = b.substring(0, b.length()-1);        	
        	bw.write(s);
        	bw.newLine();
        	
        	bw.flush();
		}
       
        bw.close();
        
        
    }
    
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		String ipPath = args[0];
		int inputCount = Integer.valueOf(args[1]);
		int outputCount = Integer.valueOf(args[2]);
		String opPath = args[3];
		
		new RunNormalizer().normalize(ipPath, inputCount, outputCount, opPath);
	}
    

          
        

}
