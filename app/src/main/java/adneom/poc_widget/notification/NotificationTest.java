package adneom.poc_widget.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import adneom.poc_widget.MainActivity;


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
            if(intent.hasExtra(MainActivity.KEY_VALUE)){
                int value = intent.getIntExtra(MainActivity.KEY_VALUE,-1);
                switch (value){
                    case 1 :
                        break;
                    case 2:
                        Intent intentSnapshot = new Intent(NotificationTest.this,MainActivity.class);
                        intentSnapshot.putExtra(MainActivity.KEY_CAN_SHOW,true);
                        startActivity(intentSnapshot);
                        break;
                    case 3 :
                        /*Intent intentClose = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                        sendBroadcast(intentClose);*/
                        break;
                    case -1:
                        break;
                }
            }
        }
    }
}
