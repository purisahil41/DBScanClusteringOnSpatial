package DBScanPackage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DoublePoint;

public class CrimeEntry implements Clusterable{

	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
	Date date = new Date();
	String block = "";
	String primaryType = "";
	String description = "";
	String locationDescription = "";
	DoublePoint point = null;
	int year = 0;


	public CrimeEntry()
	{
		dateFormat.format(date);
	}

	@Override
	public String toString()
	{
		return "Point  :" + this.point.getPoint()[0] + "|" + this.point.getPoint()[1] + ", Date : " + dateFormat.format(this.date).toString() + ", Block :" + this.block + ", primary type :" + this.primaryType + ", description :" + this.description + ", location description :" + this.locationDescription;
	}
	public CrimeEntry(String date, String block, String primaryType, String description, String locationDescription, DoublePoint point, String year)
	{
		try
		{
			this.block = block;
			this.primaryType = primaryType;
			this.description = description;
			this.locationDescription = locationDescription;
			this.point = point;
			this.year = Integer.parseInt(year);    
			this.date = dateFormat.parse(date);
		}
		catch(Exception e)
		{
			this.date = null;
		}
	}

	@Override
	public double[] getPoint() {
		return this.point.getPoint();
	}

}





