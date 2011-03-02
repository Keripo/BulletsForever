package com.beatsportable.bullets;

import java.util.ArrayList;

public class Boss {
	
	double x, y, health;
	ArrayList bullets; 		//Queue of bullets
	//Sub-class for/array list of the different parts necessary??
	
	public Boss(){
	}
	public Boss(double x){ 		// initialize y to whatever vertical distance we want from the boss
		this.x=x;
	}
	public Boss(double x, double y){  	// initialize x and y to mid point of bottom of screen
		this.x=x;
		this.y=y;
	}
	
	public void move(){
		//Update position on screen according to player input on screen
	}

	public void collisionOccured(){
		// check and decrement health and update it
		deleteBullet();
	}
	
	public void deleteBullet(){
		// remove bullet from array
	}
	
	public void shoot(){
		//Create bullet, increment in BulletsEngine
	}
	
}
