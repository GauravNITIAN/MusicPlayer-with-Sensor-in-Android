package com.hp;

import java.io.IOException;
import java.util.List;



import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Music_play extends Activity {
	 String filename;
	 Cursor audiocursor;
	 int pos;
	 int audio_column_index;
	 MediaPlayer m;
	 SensorManager sm = null; 
	 int count;
	 List list; 
	 SeekBar seekbar;
	 int timeElapsed,finalTime;
	 private Handler durationHandler = new Handler();
	 private int forwardTime = 500, backwardTime = 500;
	 ImageView image;
	 
	 //  Sensor appplication
	 SensorEventListener sel = new SensorEventListener(){  
	        public void onAccuracyChanged(Sensor sensor, int accuracy) {}                                                         
	        public void onSensorChanged(SensorEvent event) {  
	            float[] values = event.values;  
	           if(values[0]<-8)
	           {
	        	   change();
	           }
	           else if(values[0]>8)
	           {
	        	   back();
	           }
	        }  
	    };  
	/// Ends sensor declaration
	 
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_music_play);
			 seekbar = (SeekBar) findViewById(R.id.seekBar1);
				image =(ImageView) findViewById(R.id.imageView1);
				Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
 		        image.startAnimation(animation2);
			Intent i=getIntent();
			
			 Bundle extras=i.getExtras();
			 
			 filename=extras.getString("audiofilename");
			pos =extras.getInt("position");
			 
			//Toast.makeText(getApplicationContext(), filename+pos, Toast.LENGTH_LONG).show();
	  
	    	m= new MediaPlayer();
		   		
	    	   try {
	               m.setDataSource(filename);
	             }
	             
	             catch (IOException e) {
	                e.printStackTrace();
	                Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
	             }
	             
	             try {
	                m.prepare();
	             }
	             
	             catch (IOException e) {
	                e.printStackTrace();
	                Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
	             }
	             finalTime = m.getDuration();        
	             seekbar.setMax((int) finalTime);
					seekbar.setClickable(false); 
	             m.start();
	             
					timeElapsed = m.getCurrentPosition();
					seekbar.setProgress((int) timeElapsed);
					durationHandler.postDelayed(updateSeekBarTime, 100);
	          //   Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
	    	  
	             
	             String[] proj={
	      			   
	      			   MediaStore.Audio.Media._ID,
	      			   
	      			   MediaStore.Audio.Media.DATA,
	      			   
	      			   MediaStore.Audio.Media.DISPLAY_NAME,
	      			   
	      			   MediaStore.Audio.Media.SIZE,
	      	   };
	      	   
	      	   audiocursor= managedQuery(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, proj, null, null, null);
	      	   
	      	   count=audiocursor.getCount();
	      	   // Sensor Service stars here
	      	 sm = (SensorManager)getSystemService(SENSOR_SERVICE);  
	         list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);  
	         if(list.size()>0){  
	             sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);  
	         }else{  
	             Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();  
	         }  

	                
	      	   
	    	 // Sensor service ends here
	         
	         // Seek bar operation here
	         
	        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	 			
	 			@Override
	 			public void onStopTrackingTouch(SeekBar seekBar) {
	 				// TODO Auto-generated method stub
	 				
	 	//	Toast.makeText(getApplicationContext(), "OnStoptracking", Toast.LENGTH_SHORT).show();	
	 				m.seekTo(timeElapsed);
	 				m.start();
	 			
	 		 		
	 		 		 		durationHandler.postDelayed(updateSeekBarTime, 100);
	 			}
	 			
	 			@Override
	 			public void onStartTrackingTouch(SeekBar seekBar) {
	 				// TODO Auto-generated method stub
	 				
	 		// 		Toast.makeText(getApplicationContext(), "OnStarttracking", Toast.LENGTH_SHORT).show();	

	 				
	 			}
	 			
	 			@Override
	 			public void onProgressChanged(SeekBar seekBar, int progress,
	 					boolean fromUser) {
	 				// TODO Auto-generated method stub
	 				timeElapsed=progress;
	 		 //		Toast.makeText(getApplicationContext(), "Onprogress", Toast.LENGTH_SHORT).show();	
	 		 		durationHandler.postDelayed(updateSeekBarTime, 100);
	 		 		
	 		       
	 				
	 			}
	 		});
	         
		}

	public void next(View v)
	{
		audiocursor.moveToPosition(pos++);
		 if(pos>=count)
         {
       	  pos=0;
         }
		audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		String filename=audiocursor.getString(audio_column_index);
		m.stop();
		m= new MediaPlayer();
   		
 	   try {
            m.setDataSource(filename);
          }
          
          catch (IOException e) {
             e.printStackTrace();
             Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
          }
          
          try {
             m.prepare();
          }
          
          catch (IOException e) {
             e.printStackTrace();
             Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
          }
          finalTime = m.getDuration();        
          seekbar.setMax((int) finalTime);
				seekbar.setClickable(false); 
          m.start();
          timeElapsed = m.getCurrentPosition();
			seekbar.setProgress((int) timeElapsed);
			durationHandler.postDelayed(updateSeekBarTime, 100);
         
        //  Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
	}
	
	public void prev(View v)
	{
		audiocursor.moveToPosition(pos--);

		 if(pos<=0)
        {
      	  pos=0;
        }
		audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		String filename=audiocursor.getString(audio_column_index);
		
		 
   		
 	   try {
 		   
 		   m.stop();
 		  m= new MediaPlayer();
            m.setDataSource(filename);
          }
          
          catch (IOException e) {
             e.printStackTrace();
             Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
          }
          
          try {
             m.prepare();
          }
          
          catch (IOException e) {
             e.printStackTrace();
             Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
          }
          
          finalTime = m.getDuration();        
          seekbar.setMax((int) finalTime);
				seekbar.setClickable(false); 
          m.start();
          timeElapsed = m.getCurrentPosition();
			seekbar.setProgress((int) timeElapsed);
			durationHandler.postDelayed(updateSeekBarTime, 100);
         
         // Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
        
          
	}
public void pause(View v)
{
	if(m!=null && m.isPlaying())
	{
		m.pause();		
}

	else if(m!=null && m.isPlaying())
	{
		
	}
	else if(m!=null)
	{
		m.start();
	}
}






/* (non-Javadoc)
 * @see android.app.Activity#onDestroy()
 */
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	if(m.isPlaying())
	{
	m.pause();
	}
}

@Override
protected void onRestart() {
	
	super.onRestart();
	if(!m.isPlaying()&&m!=null)
	{
	m.start();
	}
}

// Sensor envents  code is here 
public void change()
{
	audiocursor.moveToPosition(pos++);
	if(pos>=count)
    {
  	  pos=0;
    }
	audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
	String filename=audiocursor.getString(audio_column_index);
	m.stop();
	m= new MediaPlayer();
	//Toast.makeText(getApplicationContext(), "Next Song", Toast.LENGTH_SHORT).show();

	
		
	   try {
        m.setDataSource(filename);
      }
      
      catch (IOException e) {
         e.printStackTrace();
         Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
      }
      
      try {
         m.prepare();
      }
      
      catch (IOException e) {
         e.printStackTrace();
         Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
      }
      
      finalTime = m.getDuration();        
      seekbar.setMax((int) finalTime);
			seekbar.setClickable(false); 
      m.start();
      timeElapsed = m.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		durationHandler.postDelayed(updateSeekBarTime, 100);
      
     
}
public void back()
{
	
	audiocursor.moveToPosition(pos--);

	 if(pos<=0)
    {
  	  pos=0;
    }
	audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
	String filename=audiocursor.getString(audio_column_index);
	
	
		
	   try {
		   
		   m.stop();
			//Toast.makeText(getApplicationContext(), "Previous Song", Toast.LENGTH_SHORT).show();

		  m= new MediaPlayer();
        m.setDataSource(filename);
      }
      
      catch (IOException e) {
         e.printStackTrace();
         Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
      }
      
      try {
         m.prepare();
      }
      
      catch (IOException e) {
         e.printStackTrace();
         Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
      }
      
      finalTime = m.getDuration();        
      seekbar.setMax((int) finalTime);
			seekbar.setClickable(false); 
      m.start();
      timeElapsed = m.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		durationHandler.postDelayed(updateSeekBarTime, 100);
}

private Runnable updateSeekBarTime = new Runnable() {
	public void run() {
		//get current position
		timeElapsed = m.getCurrentPosition();
		//set seekbar progress
		seekbar.setProgress((int) timeElapsed);
		//set time remaing
	}
};
public void forward(View v)
{
	if ((timeElapsed + forwardTime) <= finalTime) {
		timeElapsed = timeElapsed + forwardTime;

		//seek to the exact second of the track
		m.seekTo((int) timeElapsed);
	}
}
public void backward(View v)
{
	if(timeElapsed-forwardTime>0)
	{
		timeElapsed = timeElapsed - forwardTime;

		//seek to the exact second of the track
		m.seekTo((int) timeElapsed);
	}
}
}
