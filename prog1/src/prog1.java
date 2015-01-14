import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class prog1 {	
	public static void main(String[] args) {
		String[][] data = getDataFromFile("stockData.txt");
		Integer splitCount = 0;
		System.out.println("Processing INTC...");
		for(int i = 1; i < data.length; i++){
			double todayVal = Double.valueOf(data[i][5]);
			double yesterdayVal = Double.valueOf(data[i - 1][2]);
			if(isNewCompany(data[i][0], data[i-1][0])){
				System.out.println("Splits: " + splitCount);
				splitCount = 0;
				System.out.println();
				System.out.println("Processing " + data[i][0] + "...");
			}
			if(isThreeOneSplit(todayVal, yesterdayVal)){
				System.out.println("3:1 split on " + data[i][1] + "  " + data[i][5] + " --> " + data[i-1][2]);
				splitCount++;
			}
			if(isSplit(todayVal, yesterdayVal)){
				System.out.println("2:1 split on " + data[i][1] + "  " + data[i][5] + " --> " + data[i-1][2]);
				splitCount++;
			}
			if(isThreeTwoSplit(todayVal, yesterdayVal)){
				System.out.println("3:2 split on " + data[i][1] + "  " + data[i][5] + " --> " + data[i-1][2]);
				splitCount++;
			}
		}
		System.out.println("Splits: " + splitCount);
	}
		
	public static int getLineCount(String fileName){
		int lineCount = 0;
		Scanner sc = new Scanner(System.in);
		sc.close();

		try {
			sc = new Scanner(new File(fileName));
			while(sc.hasNext()){
				String[] str = sc.nextLine().split(";");
				lineCount++;
			}
			sc.close();
			return lineCount;
		}
		catch (FileNotFoundException ex) {
			System.out.println("File datafile.txt not found");
			return 0;
		}
	}
	
	public static String[][] getDataFromFile(String fileName){
		int lineCount = getLineCount(fileName);
		String[][] data = new String[lineCount][];
		Scanner sc = new Scanner(System.in);
		sc.close();
		try {
			int count = 0;
			sc = new Scanner(new File(fileName));
			while(sc.hasNext()){
				String[] line = sc.nextLine().split(";");
				data[count] = line;
				count++;
			}
			sc.close();
			return data;
		}
		catch (FileNotFoundException ex) {
			System.out.println("File datafile.txt not found");
			return data;
		}
	}
	public static boolean isSplit(double curVal, double prevVal){
        double result = (curVal / prevVal) - 2.0;
        if (result < 0.05 && result > -0.05){
            return true;
        }
        return false;
    }
	
	public static boolean isThreeTwoSplit(double curVal, double prevVal){
        double result = (curVal / prevVal) - 1.5;
        if (result < 0.05 && result > -0.05){
            return true;
        }
        return false;
    }
	
	public static boolean isThreeOneSplit(double curVal, double prevVal){
        double result = (curVal / prevVal) - 3.0;
        if (result < 0.05 && result > -0.05){
            return true;
        }
        return false;
    }
	
	public static boolean isNewCompany (String newCompany, String oldCompany){
		if (newCompany.equals(oldCompany)){
			return false;
		}
		else{
			return true;
		}
	}
	
	
}
