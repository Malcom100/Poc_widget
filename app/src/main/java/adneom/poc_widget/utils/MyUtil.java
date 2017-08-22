package adneom.poc_widget.utils;

/**
 * Created by gtshilombowanticale on 22-08-17.
 */

public class MyUtil {

    public static String base_url_google_map = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String KEY_ROUTES = "routes";
    public static final String KEY_OVERVIEW_POLYLINE = "overview_polyline";
    public static final String KEY_POINTS = "points";

    public static String addParameters(double latA,double lngA,double latB,double lngB){
        String destination = latB+","+lngB;
        String origin = latA+","+lngA;
        return String.format("%sorigin=%s&destination=%s&mode=driving",base_url_google_map,origin,destination);
    }
}
