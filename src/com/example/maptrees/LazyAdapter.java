package com.example.maptrees;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
	
	private Activity activity;
    private static List<Images> itemDetailsarrayList;
    private ArrayList<Images> arrayList;
    private LayoutInflater l_Inflater;
    
    public LazyAdapter(Context context, List<Images> images){
    	itemDetailsarrayList = images;
    	Log.i("Number",String.valueOf(images.size()));
    	this.arrayList=new ArrayList<Images>();
    	this.arrayList.addAll(images);
    	l_Inflater = LayoutInflater.from(context);

    }
    
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemDetailsarrayList.size();
	}

	@Override
	public Images getItem(int arg0) {
		// TODO Auto-generated method stub
		return itemDetailsarrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = l_Inflater.inflate(R.layout.list_row, null);
 
        TextView imageName = (TextView)vi.findViewById(R.id.imageName); // 
        TextView imageAddress = (TextView)vi.findViewById(R.id.imageAddress); //
        TextView sent=(TextView)vi.findViewById(R.id.sent);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        imageName.setText("Image Name: "+itemDetailsarrayList.get(position).get_imageName());
        imageAddress.setText("Image Address: "+itemDetailsarrayList.get(position).get_imageAddress());
        if(itemDetailsarrayList.get(position).isSent())
        	sent.setText("Sent: True");
        else
        	sent.setText("Sent: False");
       
        Bitmap bmp=BitmapFactory.decodeFile(itemDetailsarrayList.get(position).get_imageAddress());
        
        thumb_image.setImageBitmap(bmp);
     
        return vi;
    }
	
	public void filter(String text){
		Log.i("Error",text);
		itemDetailsarrayList.clear();
		if(text=="all")
			itemDetailsarrayList.addAll(arrayList);
		else if(text.equals("sent")){
			Log.i("Error!","In  sent");
			for(Images image:arrayList){
				if(image.isSent())
					itemDetailsarrayList.add(image);
			}
		}
		else if(text.equals("nonsent")){
			Log.i("Error!","In not sent");
			for(Images image:arrayList){
				Log.i("Not sent",String.valueOf(image.isSent()));
				if(image.isSent()==false)
					itemDetailsarrayList.add(image);
			}
		}
		notifyDataSetChanged();
			
	}

}