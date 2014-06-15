package geo.mapper.databasehandlers;

public class FacebookShare {
	
	private int id;
	private String userId,userName,imageAddress,postId,createdAt;
	private int likes,reShares,comments;
	
	public FacebookShare(){
		
	}
	
	public FacebookShare(String userId,String userName,String imageAddress,String postId){
		this.userId=userId;
		this.userName=userName;
		this.imageAddress=imageAddress;
		this.postId=postId;
		this.likes=0;
		this.reShares=0;
		this.comments=0;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public FacebookShare(int id,String userId,String userName,String imageAddress,String postId){
		this.id=id;
		this.userId=userId;
		this.userName=userName;
		this.imageAddress=imageAddress;
		this.postId=postId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImageAddress() {
		return imageAddress;
	}

	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getReShares() {
		return reShares;
	}

	public void setReShares(int reShares) {
		this.reShares = reShares;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}
	
	

}
