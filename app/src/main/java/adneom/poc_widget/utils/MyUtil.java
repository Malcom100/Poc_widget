package adneom.poc_widget.utils;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by gtshilombowanticale on 22-08-17.
 */

public class MyUtil {

    public static String base_url_google_map = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String KEY_ROUTES = "routes";
    public static final String KEY_OVERVIEW_POLYLINE = "overview_polyline";
    public static final String KEY_POINTS = "points";
    public static final String KEY_LEGS = "legs";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_START_LOCATION = "start_location";
    public static final String KEY_END_LOCATION = "end_location";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_POLYLINE = "polyline";
    public static final String KEY_GOOGLE = "AIzaSyBRIBlw0EX520iRnz9XHE9cr_A_JJBZ0bs";

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    public static String addParameters(double latA,double lngA,double latB,double lngB){
        String destination = latB+","+lngB;
        String origin = latA+","+lngA;
        return String.format("%sorigin=%s&destination=%s&mode=driving",base_url_google_map,origin,destination);
    }

    public static String thePointsFromOverviewPolyline(String result) {
        StringBuilder strPoints = new StringBuilder();
        try {
            JSONObject mainObject = new JSONObject(result);
            if(mainObject.has(MyUtil.KEY_ROUTES)){
                JSONArray routes = mainObject.getJSONArray(MyUtil.KEY_ROUTES);
                JSONObject firstIndex = (JSONObject) routes.get(0);
                if(firstIndex.has(MyUtil.KEY_OVERVIEW_POLYLINE)){
                    JSONObject overviewPolyline = (JSONObject) firstIndex.get(MyUtil.KEY_OVERVIEW_POLYLINE);
                    String points = (String) overviewPolyline.get(MyUtil.KEY_POINTS);
                    List<LatLng> pr = PolyUtil.decode(points);
                    for(LatLng p : pr){
                        String coord = p.latitude+","+p.longitude;
                        strPoints.append(coord);
                        strPoints.append("|");
                    }
                    strPoints.deleteCharAt((strPoints.length()-1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("E","error json object "+e.getMessage());
        }
        return strPoints.toString();
    }

    public static String thePointsFromOverviewPolylineV2(String result) {
        String urlEncoded = null;
        try {
            JSONObject mainObject = new JSONObject(result);
            if(mainObject.has(MyUtil.KEY_ROUTES)){
                JSONArray routes = mainObject.getJSONArray(MyUtil.KEY_ROUTES);
                JSONObject firstIndex = (JSONObject) routes.get(0);
                if(firstIndex.has(MyUtil.KEY_OVERVIEW_POLYLINE)){
                    JSONObject overviewPolyline = (JSONObject) firstIndex.get(MyUtil.KEY_OVERVIEW_POLYLINE);
                    String points = (String) overviewPolyline.get(MyUtil.KEY_POINTS);
                    urlEncoded = Uri.encode(points, ALLOWED_URI_CHARS);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("E","error json object "+e.getMessage());
        }
        return "enc%3A"+urlEncoded;
    }

    public static String thePointsFromSteps(String result) {
        StringBuilder strPoints = new StringBuilder();
        try {
            JSONObject mainObject = new JSONObject(result);
            if(mainObject.has(MyUtil.KEY_ROUTES)){
                JSONArray routes = mainObject.getJSONArray(MyUtil.KEY_ROUTES);
                JSONObject firstIndex = (JSONObject) routes.get(0);
                if(firstIndex.has(KEY_LEGS)){
                    JSONArray legs = firstIndex.getJSONArray(KEY_LEGS);
                    JSONObject firstLegsIndex = (JSONObject) legs.get(0);
                    if(firstLegsIndex.has(KEY_STEPS)){
                        JSONArray steps = firstLegsIndex.getJSONArray(KEY_STEPS);
                        for(int i = 0; i < steps.length(); i++){
                            JSONObject obj = steps.getJSONObject(i);
                            if(obj.has(KEY_START_LOCATION)){
                                JSONObject startLocation = obj.getJSONObject(KEY_START_LOCATION);
                                String coord = startLocation.getDouble(KEY_LAT)+","+startLocation.getDouble(KEY_LNG);
                                strPoints.append(coord);
                                strPoints.append("|");
                            }
                            if(obj.has(KEY_END_LOCATION)){
                                JSONObject endLocation = obj.getJSONObject(KEY_END_LOCATION);
                                String coord = endLocation.getDouble(KEY_LAT)+","+endLocation.getDouble(KEY_LNG);
                                strPoints.append(coord);
                                strPoints.append("|");
                            }
                            if(obj.has(KEY_POLYLINE)){
                                JSONObject polyline = obj.getJSONObject(KEY_POLYLINE);
                                if(polyline.has(KEY_POINTS)){
                                    String points = polyline.getString(KEY_POINTS);
                                    List<LatLng> pr = PolyUtil.decode(points);
                                    for(LatLng ll : pr){
                                        Log.i("Test",ll.toString());
                                    }
                                }
                            }
                        }
                    }
                }
                /*if(firstIndex.has(MyUtil.KEY_OVERVIEW_POLYLINE)){
                    JSONObject overviewPolyline = (JSONObject) firstIndex.get(MyUtil.KEY_OVERVIEW_POLYLINE);
                    String points = (String) overviewPolyline.get(MyUtil.KEY_POINTS);
                    List<LatLng> pr = PolyUtil.decode(points);
                    for(LatLng p : pr){
                        String coord = p.latitude+","+p.longitude;
                        strPoints.append(coord);
                        strPoints.append("|");
                    }
                    strPoints.deleteCharAt((strPoints.length()-1));
                }*/
                strPoints.deleteCharAt((strPoints.length()-1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("E","error json object "+e.getMessage());
        }
        return strPoints.toString();
    }
}
