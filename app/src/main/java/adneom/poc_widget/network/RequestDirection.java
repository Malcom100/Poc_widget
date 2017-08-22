package adneom.poc_widget.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import adneom.poc_widget.interfaces.IRequest;
import adneom.poc_widget.utils.MyUtil;


public class RequestDirection extends AsyncTask<String, Void, String> {

    private Context context;
    private IRequest inter;

    public RequestDirection(Context context, IRequest inter){
        this.context = context;
        this.inter = inter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader = null;
        StringBuilder str = null;
        String line = null;

        try{
            URL myURL = new URL(params[0]);
            httpsURLConnection = (HttpsURLConnection) myURL.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            Log.i("Test","code is "+httpsURLConnection.getResponseCode());
            if(httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                str = new StringBuilder();
                while( (line = bufferedReader.readLine()) != null){
                    str.append(line);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("E","error url "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E","error connection "+e.getMessage());
        }finally {
            if(httpsURLConnection != null){
                httpsURLConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("E","close buffer "+e.getMessage());
                }
            }
        }
        return str.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null && !s.isEmpty()){
            try {
                JSONObject mainObject = new JSONObject(s);
                if(mainObject.has(MyUtil.KEY_ROUTES)){
                    JSONArray routes = mainObject.getJSONArray(MyUtil.KEY_ROUTES);
                    JSONObject firstIndex = (JSONObject) routes.get(0);
                    if(firstIndex.has(MyUtil.KEY_OVERVIEW_POLYLINE)){
                        JSONObject overviewPolyline = (JSONObject) firstIndex.get(MyUtil.KEY_OVERVIEW_POLYLINE);
                        String points = (String) overviewPolyline.get(MyUtil.KEY_POINTS);
                        List<LatLng> pr = PolyUtil.decode(points);
                        for(LatLng p : pr){
                            Log.i("Test",p.toString());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("E","error json object "+e.getMessage());
            }
        }
    }
}
