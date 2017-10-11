package srt.inz.btrack;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connectors.Connectivity;
import srt.inz.connectors.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity {
	ApplicationPreference apppref;
	ListView mylist; TextView mtseattot,mtseat,mtsrc,mtdest; 
	String resdb,bresp;
	
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        mylist=(ListView)findViewById(R.id.mlist);
        mtseat=(TextView)findViewById(R.id.mtextseat);
        mtseattot=(TextView)findViewById(R.id.mtexttotal);
        mtsrc=(TextView)findViewById(R.id.mtextsrc);
        mtdest=(TextView)findViewById(R.id.mtextdest);
		apppref=(ApplicationPreference)getApplication();
		
		new LocationFetchApiTask().execute();
		new BusDataApiTask().execute();
    }
    
    public class LocationFetchApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters="id="+ URLEncoder.encode("1", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdb = Connectivity.excutePost(Constants.LOCATIONFETCH_URL,
                    urlParameters);
			Log.e("LocationFetch", resdb);
			return resdb;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			keyparser(resdb);
			//Toast.makeText(getApplicationContext(), ""+resdb, Toast.LENGTH_SHORT).show();
		}

}
	
	public void keyparser(String result)
	{
		try
		{
			JSONObject  jObject = new JSONObject(result);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String longtitude=data1.getString("longtitude");
				String lattitude=data1.getString("lattitude");
				String time=data1.getString("time");
				String status=data1.getString("status");
				
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("longtitude", longtitude);
	            map.put("lattitude", lattitude);
	            map.put("time", time);
	            map.put("status", status);
	           	            
	            map.put("notification", "Lattitude : "+lattitude+"\n Longitude : "+longtitude+
	            "\n Updated on : "+time);
	        	            
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"notification"}, new int[] {R.id.mtext_single});
	            mylist.setAdapter(adapter);
	            
	            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), 
	            		   " "+oslist.get(+position).get("status"), Toast.LENGTH_LONG).show();	               
	               
	               apppref.setMylocationLat(oslist.get(+position).get("lattitude"));
	               apppref.setMylocationLon(oslist.get(+position).get("longtitude"));
	               
	               openDialog();
				
				}
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	
	public class BusDataApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters="id="+ URLEncoder.encode("1", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			bresp = Connectivity.excutePost(Constants.BUSDETAILS_URL,
                    urlParameters);
			Log.e("BusData", bresp);
			return bresp;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			busparser();
			//Toast.makeText(getApplicationContext(), ""+bresp, Toast.LENGTH_SHORT).show();
		}

}
	
	public void busparser()
	{
		try
		{
			JSONObject  jObject = new JSONObject(bresp);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String totalseats=data1.getString("totalseats");
				String remainingseats=data1.getString("remainingseats");
				String destination=data1.getString("destination");
				String source=data1.getString("source");				
				
				mtseat.setText("Remaining Seats : "+remainingseats);
				mtseattot.setText("Total Seats : "+totalseats);
				mtsrc.setText("Source : "+source);
				mtdest.setText("Destination : "+destination);
				
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	public void openDialog(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	    	 alertDialogBuilder.setTitle("Please choose an action!");
		      alertDialogBuilder.setMessage("Do you wish to view bus location on map ? ");
	    	 alertDialogBuilder.setPositiveButton("Map View", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            	        	 
	        	 Intent i=new Intent(getApplicationContext(),Moblocator.class);
	        	 startActivity(i);
	            
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        //	 Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
	        
	        	 //finish();
	         }
	      });
	     
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
  	 
	}  

}
