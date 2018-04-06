package com.atguigu.ditu3;

public class GpsInfo {
	private String longitude;
	private String  latitude;


	public GpsInfo() {
		
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}




	@Override
	public String toString() {
		return "GpsInfo [longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}
	
	
}
