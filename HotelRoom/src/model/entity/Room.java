package model.entity;

public class Room {
	protected String roomId;
	protected String roomFloor;
	protected String roomNumber;
	protected String numberOfBeds;
	protected String status;
	protected String roomType;
	protected int beds;
	protected String imageName;
	protected String description;
	protected String createTime;
	

	public Room() {

	}

	public String getRoomFloor() {
		return roomFloor;
	}

	public void setRoomFloor(String streetNum) {
		this.roomFloor = streetNum;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String streetName) {
		this.roomNumber = streetName;
	}

	public String getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(String suburb) {
		this.numberOfBeds = suburb;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String propertyType) {
		this.roomType = propertyType;
	}

	public int getBeds() {
		return beds;
	}

	public void setBeds(int bedroomsNum) {
		this.beds = bedroomsNum;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String propertyId) {
		this.roomId = propertyId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
