package com.example.maptrees;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;




public class MainActivity extends FragmentActivity {

	private MainFragment mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}
	
	public void moveToActivity(View view){
		Intent intent =new Intent(this,UserData.class);
		startActivity(intent);

	}
}