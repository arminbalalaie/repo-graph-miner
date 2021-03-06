package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class RRWExecuter {
	public HashMap<String, String> params = new HashMap<String, String>();
	String outputDir;
	String inputFile;
	
	public RRWExecuter(String outputDir, String inputDir)
	{
		this.outputDir = outputDir;
		this.inputFile = inputDir;
		params.put("max", "35");
		params.put("min", "20");
		params.put("r", "0.1");
		params.put("overlap", "0.9");
		params.put("lambda", "0.1");
	}
	
	public void execute() throws IOException
	{
		String[] paramArr = new String[params.size() * 2 + 7];
		int i=7;
		
		for(String key: params.keySet())
		{
			paramArr[i++] = "-" + key;
			paramArr[i++] = params.get(key);
		}
		
		String outputFile = outputDir + "/out";
		
		for(String key: params.keySet())
		{
			outputFile += "_" + key + "-" + params.get(key);
		}
		
		paramArr[0] = "java";
		paramArr[1] = "-Xmx4g";
		paramArr[2] = "RRW";
		paramArr[3] = "-i";
		paramArr[4] = inputFile;
		paramArr[5] = "-o";
		paramArr[6] = outputFile;
		
		
		ProcessBuilder pb = new ProcessBuilder(paramArr);
		pb.environment().put("CLASSPATH", System.getProperty("user.dir") + "/lib/RRW");
		Process process = pb.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
	
		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}
		
		
//		is = process.getErrorStream();
//		isr = new InputStreamReader(is);
//		br = new BufferedReader(isr);
//	
//		while ((line = br.readLine()) != null) {
//		  System.out.println(line);
//		}
		
		
		
		System.out.println("\nRun finished successfully.\n");
	}
	
	public String getParamValues()
	{
		String ret = "";
		for(String key: params.keySet())
			ret += key + "\t:\t" + params.get(key) + "\n";
		
		return ret;
	}
}
