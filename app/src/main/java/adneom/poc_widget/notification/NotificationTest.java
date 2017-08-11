package adneom.poc_widget.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by gtshilombowanticale on 11-08-17.
 */

public class NotificationTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent != null){
            Log.i("Test"," test for intent : "+(intent == null));
            if(intent.hasExtra("alert")){
                Log.i("Test","intent has extra alert :)");
            }
        }
    }
}
