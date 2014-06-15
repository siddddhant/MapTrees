package geo.mapper.maptrees;

import geo.mapper.databasehandlers.DatabaseHandler;
import geo.mapper.databasehandlers.FacebookShare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.maptrees.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class UserData extends Activity {

	private final String TAG = "com.example.maptrees.TAGS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_visual);
		TextView totalSharing = (TextView) findViewById(R.id.totatPicShared);
		TextView likes = (TextView) findViewById(R.id.likes);
		TextView comments = (TextView) findViewById(R.id.comments);
		TextView shares = (TextView) findViewById(R.id.shares);
		TextView totalPics = (TextView) findViewById(R.id.totatPicTaken);
		TextView totalPicsSent = (TextView) findViewById(R.id.totatPicSent);
		Map<String, String> dataset = new HashMap<String, String>();
		dataset = calculateData();
		if (dataset != null) {
			Log.i(TAG, "Likes: " + String.valueOf(dataset.get("likes")));
			Log.i(TAG, "Comments: " + String.valueOf(dataset.get("comments")));
			Log.i(TAG, "Shares: " + String.valueOf(dataset.get("shares")));

			totalSharing.setText("Total Pics Shared: " + dataset.get("total"));
			likes.setText("Total Likes: " + dataset.get("likes"));
			comments.setText("Total Comments: " + dataset.get("comments"));
			shares.setText("Total shares " + dataset.get("shares"));
			totalPics.setText("Total Pics taken: " + dataset.get("picsTaken"));
			totalPicsSent
					.setText("Total Pics Sent: " + dataset.get("picsSent"));
			totalSharing.setVisibility(View.VISIBLE);
			likes.setVisibility(View.VISIBLE);
			comments.setVisibility(View.VISIBLE);
			shares.setVisibility(View.VISIBLE);
			totalPics.setVisibility(View.VISIBLE);
			totalPicsSent.setVisibility(View.VISIBLE);
		} else {
			totalSharing.setVisibility(View.GONE);
			likes.setVisibility(View.GONE);
			comments.setVisibility(View.GONE);
			shares.setVisibility(View.GONE);
			totalPics.setVisibility(View.GONE);
			totalPicsSent.setVisibility(View.GONE);
		}
	}

	private Map<String, String> calculateData() {
		DatabaseHandler db = new DatabaseHandler(this);
		int likes = 0, shares = 0, comments = 0;
		String imageAddress = "";
		int max = -1, maxLike = 0, maxShare = 0, maxComment = 0;
		try {
			List<FacebookShare> fb = new ArrayList<FacebookShare>();
			fb = db.getAllFacebookShareData();
			for (FacebookShare share : fb) {
				likes += share.getLikes();
				comments += share.getComments();
				shares += share.getReShares();
				if (max < (share.getComments() + share.getLikes() + share
						.getReShares())) {
					max = share.getComments() + share.getLikes()
							+ share.getReShares();
					imageAddress = share.getImageAddress();
					maxLike = share.getLikes();
					maxComment = share.getComments();
					maxShare = share.getReShares();
				}
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("likes", String.valueOf(likes));
			map.put("comments", String.valueOf(comments));
			map.put("shares", String.valueOf(shares));
			map.put("total", String.valueOf(db.getCountFacebookShares()));
			map.put("maxLikes", String.valueOf(maxLike));
			map.put("maxComments", String.valueOf(maxComment));
			map.put("maxShares", String.valueOf(maxShare));
			map.put("imageAddress", imageAddress);

			map.put("picsTaken", String.valueOf(db.getCountImagesData()));
			map.put("picsSent", String.valueOf(db.getCountSentImages()));

			return map;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void takePic(View view) {

		final GPSTracker gpsTracker = new GPSTracker(this);
		if (gpsTracker.canGetLocation() == false) {
			new AlertDialog.Builder(this)
					.setTitle("GPS OFF")
					.setMessage(
							"GPS Location is off. Click OK to change settings")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
									Intent dialogIntent = new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									dialogIntent
											.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(dialogIntent);
									if (gpsTracker.canGetLocation()) {
										Intent intent = new Intent(
												getApplicationContext(),
												Collaborate.class);
										startActivity(intent);
									} else
										return;
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					Collaborate.class);
			startActivity(intent);
		}

	}

	
	public void viewImages(View view) {
		Intent intent = new Intent(this, ViewImages.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this,MainActivity.class));
	}

}
