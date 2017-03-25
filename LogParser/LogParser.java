import java.io.*;

public class LogParser {
	
	// keep track of total TS and TJ
	private static long TSTotal;
	private static long TJTotal;
	
	public static void main(String [] args) {
		
		// start at 0
		TSTotal = 0;
		TJTotal = 0;
			
		// init reader
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			File file = new File("catalina9.out");
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String currentLine;
			
			/*long i  = 0;
			while ((currentLine = br.readLine()) != null && i< 10) {
				//parseLine (currentLine);
				System.out.println(currentLine);
				i++;
			}*/
			
			long i  = 0;
			while ((currentLine = br.readLine()) != null) {
				parseLine (currentLine);
				System.out.println(currentLine);
				i++;
			}
			System.out.println("i: "+i);
			System.out.println("Average TS: " + TSTotal / i + "ns");
			System.out.println("Average TJ: " + TJTotal / i + "ns");
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private static void parseLine(String currentLine) {
		if (currentLine.split("doGet ").length  > 1)
		{
			String values = currentLine.split("doGet ")[1];
		

			String TSString = values.split(",")[0];
			String TJString = values.split(",")[1];
			
			int TSInt = Integer.parseInt(TSString.substring(1,TSString.length()));
			int TJInt = Integer.parseInt(TJString.substring(0, TJString.length()-1));
			
			//System.out.println("TS: " + TSInt);
			//System.out.println("TJ: " + TJInt);
			
			TSTotal += TSInt;
			TJTotal += TJInt;
		}
	}
}




