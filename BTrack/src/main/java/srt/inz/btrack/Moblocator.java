package srt.inz.btrack;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi") public class Moblocator extends FragmentActivity implements OnMapReadyCallback {
	
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	
	LatLng llg;
	
	String shlat,shlong;
	ApplicationPreference apppref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maploc);
		apppref=(ApplicationPreference)getApplication();
		
		//shlat="8.49975"; shlong="76.953916";
		shlat=apppref.getMylocationLat(); shlong=apppref.getMylocationLon();
		
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
		getSupportFragmentManager().findFragmentById(R.id.mapp);

		// Getting a reference to the map
		supportMapFragment.getMapAsync(this);
		
		// Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);
          
        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				llg = new LatLng(Double.valueOf(shlat), Double.valueOf(shlong));
			       drawMarker(llg);
		        
			}
		};
		
		// Setting button click event listener for the find button
		btn_find.setOnClickListener(findClickListener);	
	}
	
	
	private void drawMarker(LatLng point) {
	    // Creating an instance of MarkerOptions
	   // MarkerOptions markerOptions = new MarkerOptions();

	    MarkerOptions markerOptionsc=new MarkerOptions();
	    
	 //   markerOptions.position(point);

	    markerOptionsc.position(llg);
	    markerOptionsc.title("center");

	    double radiusInMeters = 200.0;
	     //red outline
	    int strokeColor = 0xffff0000;
	    //opaque red fill
	    int shadeColor = 0x44ff0000; 

	    CircleOptions circleOptions = new CircleOptions().center(llg).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
	    googleMap.addCircle(circleOptions);
	    // Adding marker on the Google Map
	  //  googleMap.addMarker(markerOptions);
	   
	    CameraUpdate center=CameraUpdateFactory.newLatLng(llg);
	        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);

	        googleMap.moveCamera(center);
	        googleMap.animateCamera(zoom);
	            
	        final Circle circle = googleMap.addCircle(new CircleOptions().center(llg)
	                .strokeColor(Color.CYAN).radius(1000));

	      ValueAnimator vAnimator = new ValueAnimator();
	        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
	        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
	        vAnimator.setIntValues(0, 100);
	        vAnimator.setDuration(1000);
	        vAnimator.setEvaluator(new IntEvaluator());
	        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
	        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	            @Override
	            public void onAnimationUpdate(ValueAnimator valueAnimator) {
	                float animatedFraction = valueAnimator.getAnimatedFraction();
	                // Log.e("", "" + animatedFraction);
	                circle.setRadius(animatedFraction * 100);
	            }
	        });
	       vAnimator.start();        
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

	}
}
