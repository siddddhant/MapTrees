package com.example.maptrees;

public class Images {
	private int _id;
	private String _imageName,_imageAddress;
	private boolean sent;
	public Images(){
		
	}
	public Images(int id,String imageName,String imageAddress,boolean sent){
		this._id=id;
		this._imageName=imageName;
		this._imageAddress=imageAddress;
		this.sent=sent;
	}
	public Images(String imageName,String imageAddress,boolean sent){
		this._imageName=imageName;
		this._imageAddress=imageAddress;
		this.sent=sent;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String get_imageName() {
		return _imageName;
	}
	public void set_imageName(String _imageName) {
		this._imageName = _imageName;
	}
	public String get_imageAddress() {
		return _imageAddress;
	}
	public void set_imageAddress(String _imageAddress) {
		this._imageAddress = _imageAddress;
	}
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	

}
