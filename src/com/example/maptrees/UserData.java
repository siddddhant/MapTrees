package com.example.maptrees;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



public class UserData extends Activity {

	private final String TAG="com.example.maptrees.TAGS";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_visual);
	}
	
	public void takePic(View view){
		Intent intent=new Intent(this,Collaborate.class);
		startActivity(intent);
		
		
	}
	
	public void viewImages(View view){
		Intent intent = new Intent(this,ViewImages.class);
		startActivity(intent);
	}

}	
