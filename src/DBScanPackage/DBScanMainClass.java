package DBScanPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Exception;
import java.awt.Point;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;





public class DBScanMainClass {

	public static void main(String[] args) throws Exception {
		
		/*String userName = "username";
		String password = "password";

		String url = "jdbc:sqlserver:\\SAHIL\\SQLEXPRESS;databaseName=CHeckPlease;integratedSecurity=true";

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection(url);
		   System.out.println("connected");*/
		
		
		BufferedReader crunchifyBuffer = null;
		
		try {
			String crunchifyLine;
			ArrayList<String> valuesCSV = new ArrayList<String>(); 
			crunchifyBuffer = new BufferedReader(new FileReader("F:\\University Of Waterloo\\CS846_SoftwareEngineeringForBigData\\Final_Project\\Final_CSV_Data.csv"));
			
			// How to read file in java line by line?
			while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {
				
				//System.out.println("Converted ArrayList data: " + crunchifyCSVtoArrayList(crunchifyLine) + "\n");
				valuesCSV.add(crunchifyLine);
			}
			Collection<Clusterable> arrPoint = new ArrayList<Clusterable>();
			 
			for(int i = 1; i < valuesCSV.size(); i++)
			{
				String str = valuesCSV.get(i);
				ArrayList<String> strList = crunchifyCSVtoArrayList(str);
				if(strList.size() >= 20)
				{
					String latitude = strList.get(19); 
					String longitude = strList.get(20);
					double[] db = {Double.parseDouble(latitude), Double.parseDouble(longitude)};
					DoublePoint x = new DoublePoint(db);
				
					arrPoint.add(x);	
				}
				
			}
				
				DBSCANClusterer<Clusterable> dbScanCLusterable = new DBSCANClusterer<>(5,0, new HonaluluMeasure());
				java.util.List<Cluster<Clusterable>> adf = dbScanCLusterable.cluster(arrPoint);
			System.out.println("Size " + adf.size());
			boolean blue = false;
			if(blue== false)
			{
				blue = true;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (crunchifyBuffer != null) crunchifyBuffer.close();
			} catch (IOException crunchifyException) {
				crunchifyException.printStackTrace();
			}
		}
	}
	
	public static ArrayList<String> crunchifyCSVtoArrayList(String crunchifyCSV) {
		ArrayList<String> crunchifyResult = new ArrayList<String>();
		
		if (crunchifyCSV != null) {
			String[] splitData = crunchifyCSV.split("\\s*,\\s*");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					crunchifyResult.add(splitData[i].trim());
				}
			}
		}
		
		return crunchifyResult;
	}

}
