import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class assignment2 {	
public static Connection conn;
	
	public static void main(String[] args) {
		try {
			Scanner connStrSource = new Scanner(new File("connString.txt"));
			String[] connString = connStrSource.nextLine().trim().split("\\s+");
			connStrSource.close();
			conn = DriverManager.getConnection(connString[0].trim(), connString[1].trim(), connString[2].trim());
			System.out.println("Database connection established");
			Scanner keyboard = new Scanner(System.in);
			System.out.print("Enter a ticker symbol: ");
			String ticker = keyboard.nextLine().trim();
			while (!ticker.matches("")) {
				processTicker(ticker);
				System.out.print("\nEnter a ticker symbol: ");
				ticker = keyboard.nextLine().trim();
			}
			conn.close();
			System.out.println("Database connection closed");
		}
		catch (FileNotFoundException ex) {
			System.out.println("File connString.txt not found");
			return;
		}
		catch (SQLException ex) {
			System.out.println("SQL exception");
			ex.printStackTrace();
			return;
		}
	}
	
	private static void processTicker(String ticker) {
		ArrayList<TickerData> dataArray = new ArrayList<TickerData>();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from company where ticker = " + quote(ticker));
			if (!rs.next())
				System.out.println("Ticker " + ticker + " not found in database");
			else {
				System.out.println(rs.getString(2));
			}
			rs = stat.executeQuery("select * from pricevolume where ticker = " + quote(ticker) + " order by transDate DESC");
			if (!rs.next()) 
				System.out.println("\nNo data for ticker " + ticker);
			else {
				System.out.println("\nData for ticker " + ticker + ":");
				int n = rs.getMetaData().getColumnCount();
				System.out.print("Count ");
				for (int i = 1; i <= n; i++)
					System.out.print(String.format("%10s ", rs.getMetaData().getColumnName(i)));
				System.out.println();
				boolean done = false;
				int cnt = 0;
				while (!done) {
					TickerData data = new TickerData();
					data.date = rs.getString(2).trim();
					data.open = Double.parseDouble(rs.getString(3).trim());
					data.close = Double.parseDouble(rs.getString(6));
					dataArray.add(0, data);
					cnt = cnt + 1;
					System.out.print(String.format("%5d ", cnt));
					for (int i = 1; i <= n; i++)
						System.out.print(String.format("%10s ", rs.getString(i).trim()));
					System.out.println();
					if (!rs.next())
						done = true;
				}
				System.out.println("\nData tuples stored: " + dataArray.size());
				for (int i = 1; i < dataArray.size(); i++){
					if(isSplit(dataArray.get(i-1).close, dataArray.get(i).open)){
						System.out.println("2:1 split on " + dataArray.get(i-1).date + ";   " + dataArray.get(i-1).close + " -> " + dataArray.get(i).open);
					}
					
					//System.out.println(String.format("     %s, %7.2f, %7.2f", dataArray.get(i).date, dataArray.get(i).open, dataArray.get(i).close));
				}
				System.out.println();
			}
		}
		catch (SQLException ex) {
			System.out.println("SQL exception in processTicker");
		}
	}
	
	private static String quote(String str) {
		return "'" + str + "'";
	}
		
	public static boolean isSplit(double curVal, double prevVal){
        double result = (curVal / prevVal) - 2.0;
        if (result < 0.13 && result > -0.13){
            return true;
        }
        return false;
    }
	
	public static boolean isThreeTwoSplit(double curVal, double prevVal){
        double result = (curVal / prevVal) - 1.5;
        if (result < 0.13 && result > -0.13){
            return true;
        }
        return false;
    }
	
	public static boolean isThreeOneSplit(double curVal, double prevVal){
        double result = (curVal / prevVal) - 3.0;
        if (result < 0.13 && result > -0.13){
            return true;
        }
        return false;
    }
}
