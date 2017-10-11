package srt.inz.btrack;


import srt.inz.connectors.Constants;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits") public class ApplicationPreference extends Application {
    private static SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
String Name,Mylocationlat,Mylocationlon;

    public String getId() {
         String userid=  appSharedPrefs.getString(Constants.USERID,"");
        return userid;
    }

    public void setId(String id) {
        prefsEditor.putString(Constants.USERID, id);
        prefsEditor.commit();
    }

    public String getMylocationLat() {
    	Mylocationlat=appSharedPrefs.getString(Constants.MYLOCATONLAT
    			,"");
        return Mylocationlat;
    }

    public void setMylocationLat(String loc) {
        prefsEditor.putString(Constants.MYLOCATONLAT, loc);
        prefsEditor.commit();
    }
    public String getMylocationLon() {
    	Mylocationlon=appSharedPrefs.getString(Constants.MYLOCATONLON,"");
        return Mylocationlon;
    }

    public void setMylocationLon(String loc) {
        prefsEditor.putString(Constants.MYLOCATONLON, loc);
        prefsEditor.commit();
    }

	@Override
    public void onCreate() {
        super.onCreate();
        this.appSharedPrefs = getApplicationContext().getSharedPreferences(
                Constants.PREFERENCE_PARENT, MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

}
