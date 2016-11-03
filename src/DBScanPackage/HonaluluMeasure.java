package DBScanPackage;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

public class HonaluluMeasure implements DistanceMeasure {

	@Override
	public double compute(double[] arg0, double[] arg1)
	{
	 final int R = 6371; // Radious of the earth
    Double lat1 = arg0[0];
    Double lon1 = arg0[1];
    Double lat2 = arg1[0];
    Double lon2 = arg1[1];
    Double latDistance = toRad(lat2-lat1);
    Double lonDistance = toRad(lon2-lon1);
    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
               Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
               Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    Double distance = R * c;
    return distance;
     
	}


private static Double toRad(Double value) {
    return value * Math.PI / 180;
}

}



