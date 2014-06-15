package geo.mapper.maptrees;

import java.util.HashMap;

import com.example.maptrees.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.view.View.OnClickListener;

;;

public class Questionnaire extends Activity implements OnCheckedChangeListener {

	// private HashMap<String,String> quest1=new HashMap<String,String>();
	// private HashMap<String,String> quest2=new HashMap<String,String>();
	Bundle quest1=new Bundle();
	Bundle quest2=new Bundle();
	private boolean volun = false, pubGood = false, helpNat = false,
			oth = false, invol = false, conserv = false, fac = false;
	private boolean volun1 = false, pubGood1 = false, helpNat1 = false,
			oth1 = false, invol1 = false, conserv1 = false, fac1 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		if (bundle!=null && bundle.containsKey("doneQ1")) {
			setContentView(R.layout.questionare_2);
			
			RadioGroup rVolunteer1 = (RadioGroup) findViewById(R.id.voluntariness1);
			RadioGroup publicGood1 = (RadioGroup) findViewById(R.id.public_good1);
			RadioGroup helpingNature1 = (RadioGroup) findViewById(R.id.helping_nature1);
			RadioGroup others1 = (RadioGroup) findViewById(R.id.others1);
			RadioGroup involvement1 = (RadioGroup) findViewById(R.id.involvement1);
			RadioGroup conserv1 = (RadioGroup) findViewById(R.id.conservationists1);
			RadioGroup inducement1 = (RadioGroup) findViewById(R.id.inducement);
			
			publicGood1.setOnCheckedChangeListener(this);
			helpingNature1.setOnCheckedChangeListener(this);
			others1.setOnCheckedChangeListener(this);
			involvement1.setOnCheckedChangeListener(this);
			conserv1.setOnCheckedChangeListener(this);
			inducement1.setOnCheckedChangeListener(this);
			rVolunteer1.setOnCheckedChangeListener(this);
			
			findViewById(R.id.sendSurvey).setVisibility(View.GONE);

		} else {
			setContentView(R.layout.questionare);
			
			RadioGroup rVolunteer = (RadioGroup) findViewById(R.id.voluntariness);
			RadioGroup publicGood = (RadioGroup) findViewById(R.id.public_good);
			RadioGroup helpingNature = (RadioGroup) findViewById(R.id.helping_nature);
			RadioGroup others = (RadioGroup) findViewById(R.id.others);
			RadioGroup involvement = (RadioGroup) findViewById(R.id.involvement);
			RadioGroup conserv = (RadioGroup) findViewById(R.id.conservationists);
			RadioGroup inducement = (RadioGroup) findViewById(R.id.asked_by_faculty);

			publicGood.setOnCheckedChangeListener(this);
			helpingNature.setOnCheckedChangeListener(this);
			others.setOnCheckedChangeListener(this);
			involvement.setOnCheckedChangeListener(this);
			conserv.setOnCheckedChangeListener(this);
			inducement.setOnCheckedChangeListener(this);
			rVolunteer.setOnCheckedChangeListener(this);
			
			findViewById(R.id.nextQ).setVisibility(View.GONE);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		Log.i("data", String.valueOf(checkedId));
		Log.i("data", String.valueOf(R.id.public_good));
		switch (checkedId) {
		case R.id.v_yes:
			volun = true;
			quest1.putString("voluntriness", "true");
			break;
		case R.id.v_no:
			volun = true;
			quest1.putString("voluntriness", "false");
			break;
		case R.id.d_yes:
			pubGood = true;
			quest1.putString("public good", "true");
			break;
		case R.id.d_no:
			pubGood = true;
			quest1.putString("public good", "false");
			break;
		case R.id.h_yes:
			helpNat = true;
			quest1.putString("helping nature", "good");
			break;
		case R.id.h_no:
			helpNat = true;
			quest1.putString("helping nature", "false");
			break;
		case R.id.o_yes:
			oth = true;
			quest1.putString("others", "true");
			break;
		case R.id.o_no:
			oth = true;
			quest1.putString("others", "false");
			break;
		case R.id.i_yes:
			invol = true;
			quest1.putString("involvement", "true");
			break;
		case R.id.i_no:
			invol = true;
			quest1.putString("involvement", "false");
			break;
		case R.id.co_yes:
			conserv = true;
			quest1.putString("contact with nature", "true");
			break;
		case R.id.co_no:
			conserv = true;
			quest1.putString("contact with nature", "false");
			break;
		case R.id.f_yes:
			fac = true;
			quest1.putString("faculty", "true");
			break;
		case R.id.f_no:
			fac = true;
			quest1.putString("faculty", "false");
			break;
			
		case R.id.v_yes1:
			volun1 = true;
			quest2.putString("voluntriness", "true");
			break;
		case R.id.v_no1:
			volun1 = true;
			quest2.putString("voluntriness", "false");
			break;
		case R.id.d_yes1:
			pubGood1 = true;
			quest2.putString("public good", "true");
			break;
		case R.id.d_no1:
			pubGood1 = true;
			quest2.putString("public good", "false");
			break;
		case R.id.h_yes1:
			helpNat1 = true;
			quest2.putString("helping nature", "good");
			break;
		case R.id.h_no1:
			helpNat1 = true;
			quest2.putString("helping nature", "false");
			break;
		case R.id.o_yes1:
			oth1 = true;
			quest2.putString("others", "true");
			break;
		case R.id.o_no1:
			oth1 = true;
			quest2.putString("others", "false");
			break;
		case R.id.i_yes1:
			invol1 = true;
			quest2.putString("involvement", "true");
			break;
		case R.id.i_no1:
			invol1 = true;
			quest2.putString("involvement", "false");
			break;
		case R.id.co_yes1:
			conserv1 = true;
			quest2.putString("contact with nature", "true");
			break;
		case R.id.co_no1:
			conserv1 = true;
			quest2.putString("contact with nature", "false");
			break;
		case R.id.f_yes1:
			fac1 = true;
			quest2.putString("faculty", "true");
			break;
		case R.id.f_no1:
			fac1 = true;
			quest2.putString("faculty", "false");
			break;
		default:
			Toast.makeText(this, "la", Toast.LENGTH_SHORT).show();
			break;

		}

		if (volun && pubGood && helpNat && oth && conserv && invol && fac) {
			
			Intent i = new Intent(this, Questionnaire.class);
			i.putExtra("quest1", quest1);
			i.putExtra("doneQ1", true);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

		}
		else if (volun1 && pubGood1 && helpNat1 && oth1 && conserv1 && invol1 && fac1) {
			findViewById(R.id.sendSurvey).setVisibility(View.VISIBLE);
		}

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void goNextPage(View view){
		Intent i = new Intent(this, Questionnaire.class);
		i.putExtra("quest1", quest1);
		i.putExtra("doneQ1", true);
		startActivity(i);
		  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

			}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("Move to Main Page")
		.setMessage("Are you sure you want to go back to main page?")
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	startActivity(new Intent(getApplicationContext(),MainActivity.class));
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	

}
