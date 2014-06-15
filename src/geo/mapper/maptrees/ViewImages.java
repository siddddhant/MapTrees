package geo.mapper.maptrees;

import geo.mapper.databasehandlers.DatabaseHandler;

import java.util.List;

import com.example.maptrees.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewImages extends Activity{
	
	private String currentCondition="all";
	Button allImages;
	Button sentImages;
	Button notSentImages;
	LazyAdapter adaptor;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this,UserData.class));
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_images);
		allImages=(Button)findViewById(R.id.allImages);
		sentImages=(Button)findViewById(R.id.sentImages);
		notSentImages=(Button)findViewById(R.id.nonSentImages);
		Log.i("Downloading data","here");
		DatabaseHandler db=new DatabaseHandler(this);
		List<Images> images=db.getAllImageData();
		final ListView lv=(ListView)findViewById(R.id.listview);
		Log.i("Setting adaptrt","adapter setting");
		adaptor=new LazyAdapter(this,images);
		lv.setAdapter(adaptor);
		
		allImages.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				adaptor.filter("all");
				
			}
			
		});
		
		sentImages.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.i("Error!","here");
				adaptor.filter("sent");
				
			}
			
		});
		
		notSentImages.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.i("Error!","here");
				adaptor.filter("nonsent");
				
			}
			
		});
		
		
	}
	
	

}


