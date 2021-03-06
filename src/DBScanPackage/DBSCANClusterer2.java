package DBScanPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.util.MathUtils;

/**
* DBSCAN (density-based spatial clustering of applications with noise) algorithm.
* <p>
* The DBSCAN algorithm forms clusters based on the idea of density connectivity, i.e.
* a point p is density connected to another point q, if there exists a chain of
* points p<sub>i</sub>, with i = 1 .. n and p<sub>1</sub> = p and p<sub>n</sub> = q,
* such that each pair &lt;p<sub>i</sub>, p<sub>i+1</sub>&gt; is directly density-reachable.
* A point q is directly density-reachable from point p if it is in the &epsilon;-neighborhood
* of this point.
* <p>
* Any point that is not density-reachable from a formed cluster is treated as noise, and
* will thus not be present in the result.
* <p>
* The algorithm requires two parameters:
* <ul>
*   <li>eps: the distance that defines the &epsilon;-neighborhood of a point
*   <li>minPoints: the minimum number of density-connected points required to form a cluster
* </ul>
*
* @param <T> type of the points to cluster
* @see <a href="http://en.wikipedia.org/wiki/DBSCAN">DBSCAN (wikipedia)</a>
* @see <a href="http://www.dbs.ifi.lmu.de/Publikationen/Papers/KDD-96.final.frame.pdf">
* A Density-Based Algorithm for Discovering Clusters in Large Spatial Databases with Noise</a>
* @since 3.2
*/
public class DBSCANClusterer2<T extends Clusterable> extends Clusterer<T> {

   /** Maximum radius of the neighborhood to be considered. */
   private final double              eps;

   /** Minimum number of points needed for a cluster. */
   private final int                 minPts;

   /** Status of a point during the clustering process. */
   private enum PointStatus {
       /** The point has is considered to be noise. */
       NOISE,
       /** The point is already part of a cluster. */
       PART_OF_CLUSTER
   }

   /**
    * Creates a new instance of a DBSCANClusterer.
    * <p>
    * The euclidean distance will be used as default distance measure.
    *
    * @param eps maximum radius of the neighborhood to be considered
    * @param minPts minimum number of points needed for a cluster
    * @throws NotPositiveException if {@code eps < 0.0} or {@code minPts < 0}
    */
   public DBSCANClusterer2(final double eps, final int minPts)
       throws NotPositiveException {
       this(eps, minPts, new EuclideanDistance());
   }

   /**
    * Creates a new instance of a DBSCANClusterer.
    *
    * @param eps maximum radius of the neighborhood to be considered
    * @param minPts minimum number of points needed for a cluster
    * @param measure the distance measure to use
    * @throws NotPositiveException if {@code eps < 0.0} or {@code minPts < 0}
    */
   public DBSCANClusterer2(final double eps, final int minPts, final DistanceMeasure measure)
       throws NotPositiveException {
       super(measure);

       if (eps < 0.0d) {
           throw new NotPositiveException(eps);
       }
       if (minPts < 0) {
           throw new NotPositiveException(minPts);
       }
       this.eps = eps;
       this.minPts = minPts;
   }

   /**
    * Returns the maximum radius of the neighborhood to be considered.
    * @return maximum radius of the neighborhood
    */
   public double getEps() {
       return eps;
   }

   /**
    * Returns the minimum number of points needed for a cluster.
    * @return minimum number of points needed for a cluster
    */
   public int getMinPts() {
       return minPts;
   }

   /**
    * Performs DBSCAN cluster analysis.
    *
    * @param points the points to cluster
    * @return the list of clusters
    * @throws NullArgumentException if the data points are null
    */
   @Override
   public List<Cluster<T>> cluster(final Collection<T> crimeEntryList) throws NullArgumentException {

       // sanity checks
       MathUtils.checkNotNull(crimeEntryList);

       final List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
       final Map<Clusterable, PointStatus> visited = new HashMap<Clusterable, PointStatus>();

       for (final T crimeEntry : crimeEntryList) {
           if (visited.get(((CrimeEntry)crimeEntry).point) != null) {
               continue;
           }
           final List<T> neighbors = getNeighbors(((CrimeEntry)crimeEntry).point, crimeEntryList);
           if (neighbors.size() >= minPts) {
               // DBSCAN does not care about center points
               final Cluster<T> cluster = new Cluster<T>();
               clusters.add(expandCluster(cluster, crimeEntry, neighbors, crimeEntryList, visited));
           } else {
               visited.put(((CrimeEntry)crimeEntry).point, PointStatus.NOISE);
           }
       }

       return clusters;
   }

   /**
    * Expands the cluster to include density-reachable items.
    *
    * @param cluster Cluster to expand
    * @param point Point to add to cluster
    * @param neighbors List of neighbors
    * @param points the data set
    * @param visited the set of already visited points
    * @return the expanded cluster
    */
   private Cluster<T> expandCluster(final Cluster<T> cluster,
                                    final T crimeEntry,
                                    final List<T> neighbors,
                                    final Collection<T> crimeEntryList,
                                    final Map<Clusterable, PointStatus> visited) {
       cluster.addPoint(crimeEntry);
       visited.put(((CrimeEntry)crimeEntry).point, PointStatus.PART_OF_CLUSTER);

       List<T> seeds = new ArrayList<T>(neighbors);
       int index = 0;
       while (index < seeds.size()) {
           final T current = seeds.get(index);
           PointStatus pStatus = visited.get(((CrimeEntry)current).point);
           // only check non-visited points
           if (pStatus == null) {
               final List<T> currentNeighbors = getNeighbors(((CrimeEntry)current).point, crimeEntryList);
               if (currentNeighbors.size() >= minPts) {
                   seeds = merge(seeds, currentNeighbors);
               }
           }

           if (pStatus != PointStatus.PART_OF_CLUSTER) {
               visited.put(((CrimeEntry)current).point, PointStatus.PART_OF_CLUSTER);
               cluster.addPoint(current);
           }

           index++;
       }
       return cluster;
   }

   /**
    * Returns a list of density-reachable neighbors of a {@code point}.
    *
    * @param point the point to look for
    * @param points possible neighbors
    * @return the List of neighbors
    */
   private List<T> getNeighbors(final DoublePoint point, final Collection<T> crimeEntryList) {
       final List<T> neighbors = new ArrayList<T>();
       for (final T crimeEntry : crimeEntryList) {
           if (point != ((CrimeEntry)crimeEntry).point && distance(((CrimeEntry)crimeEntry).point, point) <= eps) {
               neighbors.add(crimeEntry);
           }
       }
       return neighbors;
   }

   /**
    * Merges two lists together.
    *
    * @param one first list
    * @param two second list
    * @return merged lists
    */
   private List<T> merge(final List<T> one, final List<T> two) {
       final Set<T> oneSet = new HashSet<T>(one);
       for (T item : two) {
           if (!oneSet.contains(item)) {
               one.add(item);
           }
       }
       return one;
   }
}
