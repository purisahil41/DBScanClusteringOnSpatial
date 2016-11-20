package DBScanPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.Exception;
import java.awt.Point;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

//import geo.google.datamodel;




public class DBScanMainClass {

	public static void main(String[] args) throws Exception {

		// Read CSV and Load data
		BufferedReader crunchifyBuffer = null;
		try {
			String crunchifyLine;
			ArrayList<String> valuesCSV = new ArrayList<String>(); 
			crunchifyBuffer = new BufferedReader(new FileReader("F:\\University Of Waterloo\\CS846_SoftwareEngineeringForBigData\\Final_Project\\CSV_Data_100.csv"));

			// How to read file in java line by line?
			while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {

				//System.out.println("Converted ArrayList data: " + crunchifyCSVtoArrayList(crunchifyLine) + "\n");
				valuesCSV.add(crunchifyLine);
			}
			Collection<Clusterable> arrPoint = new ArrayList<Clusterable>();
			List crimeEntryList = new ArrayList<CrimeEntry>();
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
					// List of All crimes
					crimeEntryList.add(new CrimeEntry(strList.get(2), strList.get(3), strList.get(5), strList.get(6),strList.get(7),  x, strList.get(17)));
				}

			}

			// DBScan Implementation
			DBSCANClusterer2<Clusterable> dbScanCLusterable = new DBSCANClusterer2<>(0.5,1, new HonaluluMeasure());
			java.util.List<Cluster<Clusterable>> adf = dbScanCLusterable.cluster(crimeEntryList);
			System.out.println("Size " + adf.size());

			// Temp output to check the details of clusters
			BufferedWriter output = null;
			File file = new File("F:\\University Of Waterloo\\CS846_SoftwareEngineeringForBigData\\Final_Project\\CentroidList_100.txt");
			FileOutputStream is = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer writer = new BufferedWriter(osw);


			// Calculation of centroid for each cluster
			double x = 0;
			double y = 0;
			double z = 0;
			Hashtable centroidList = new Hashtable();// DoublePoint[adf.size()];
			for(int i = 0; i < adf.size(); i++)
			{
				Cluster currCluster = adf.get(i);
				List pointList = currCluster.getPoints(); 
				writer.write("Cluster  :" + (i + 1) + " \n");
				for(int j =0; j <pointList.size();j++)
				{
					CrimeEntry currPoint = (CrimeEntry)pointList.get(j);
					double[] pointCoordinates  = currPoint.point.getPoint();
					writer.write((j + 1) + ". " + currPoint.toString() + "\n");	
					double latitude = pointCoordinates[0] * Math.PI / 180;
					double longitude = pointCoordinates[1] * Math.PI / 180;

					x += Math.cos(latitude) * Math.cos(longitude);
					y += Math.cos(latitude) * Math.sin(longitude);
					z += Math.sin(latitude);
				}

				x = x / pointList.size();
				y = y / pointList.size();
				z = z / pointList.size();

				double centralLongitude = Math.atan2(y, x);
				double centralSquareRoot = Math.sqrt(x * x + y * y);
				double centralLatitude = Math.atan2(z, centralSquareRoot);

				double[] db = {centralLatitude * 180 / Math.PI, centralLongitude * 180 / Math.PI};
				centroidList.put(new DoublePoint(db), "");

				writer.write("Cluster Centroid Point  :" + db[0] + "," + db[1] + "\n");
				writer.write("*******************************\n");

			}
			
			

			writer.flush();
			writer.close();

			
			
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
