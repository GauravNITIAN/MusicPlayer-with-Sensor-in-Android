package com.hp;




import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	ListView lv;
	 SensorManager sm = null; 
	private Cursor audiocursor;
	private int audio_column_index;
	int count;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       
        
        init_phone_audio_grid();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void init_phone_audio_grid()
    {
    	   System.gc();//garbage collector
    	   
    	   //make the aaray of object to store the value of the vedios fetched from the sdcard
    	   
    	   String[] proj={
    			   
    			   MediaStore.Audio.Media._ID,
    			   
    			   MediaStore.Audio.Media.DATA,
    			   
    			   MediaStore.Audio.Media.DISPLAY_NAME,
    			   
    			   MediaStore.Audio.Media.SIZE,
    	   };
    	   
    	   audiocursor= managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
    	   
    	   count=audiocursor.getCount();
    	   
    	   lv=(ListView) findViewById(R.id.listView1);
    	   
    	   lv.setAdapter(new AudioAdapter(getApplicationContext()));
    	   
    	   lv.setOnItemClickListener(audiogridlistener);
    	
    }
    
    private OnItemClickListener audiogridlistener=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			
			audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			audiocursor.moveToPosition(arg2);
			
			
			
			String filename=audiocursor.getString(audio_column_index);
			
			Intent i=new Intent(MainActivity.this,Music_play.class);
			
			i.putExtra("audiofilename", filename);
			i.putExtra("position", arg2);
			
			startActivity(i);
			
		}};
    
		public class AudioAdapter extends BaseAdapter{
			
			private Context vContext;
			
			
			public AudioAdapter(Context c)
			{
				
				vContext=c;
			}
			
			

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return count;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				TextView tv=new TextView(vContext.getApplicationContext());
				String id=null;
				if(convertView==null)
				{
					audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
					audiocursor.moveToPosition(position);
					id=audiocursor.getString(audio_column_index);
					audio_column_index=audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
					audiocursor.moveToPosition(position);
					//id+="SIZE(KB): "+audiocursor.getString(audio_column_index);
					tv.setText(id);		
				}
				else
					
					tv=(TextView) convertView;
				return tv;
			}}
		
}
