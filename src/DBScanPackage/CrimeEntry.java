package DBScanPackage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.math3.ml.clustering.DoublePoint;

public class CrimeEntry {

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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

}





