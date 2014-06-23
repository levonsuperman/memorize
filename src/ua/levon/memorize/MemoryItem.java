package ua.levon.memorize;

import android.widget.ImageView;

public class MemoryItem {

	private String title;
	private String description;
	private ImageView photo;

	MemoryItem (String title, String description, ImageView photo) {
		
		this.title=title;
		this.description=description;
		this.photo=photo;
		
	}
	
	void set (String title, String description, ImageView photo) {
		
		this.title=title;
		this.description=description;
		this.photo=photo;
		
	}
	
	String getTitle() {
		
		return title;
		
	}
	
	String getDescription() {
		
		return description;
		
	}
	
	ImageView getPhoto() {
		
		return photo;
		
	}



}
