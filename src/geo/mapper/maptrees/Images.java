package geo.mapper.maptrees;

public class Images {
	private int _id;
	private String _imageName,_imageAddress;
	private String latitude,longitude,tag;
	private boolean sent,shared;
	public Images(){
		
	}
	public Images(int id,String imageName,String imageAddress,boolean sent,boolean shared,String latitude,String longitude,String tag){
		this._id=id;
		this._imageName=imageName;
		this._imageAddress=imageAddress;
		this.sent=sent;
		this.shared=shared;
		this.latitude=latitude;
		this.longitude=longitude;
		this.tag=tag;
	}
	public Images(String imageName,String imageAddress,boolean sent,boolean shared,String latitude,String longitude,String tag){
		this._imageName=imageName;
		this._imageAddress=imageAddress;
		this.sent=sent;
		this.shared=shared;
		this.latitude=latitude;
		this.longitude=longitude;
		this.tag=tag;
	}
	public String getLatitude() {
		return latitude;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
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
