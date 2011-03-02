package com.beatsportable.bullets;

public class BulletsEngine {
	/*Global variables:
	 *x- x coordinate
	 *y- y coordinate
	 *angle- angle with which it was fired, WRT the ground.
	 *
	 *
	 */

	double x, y, angle;
	char c; // c = 'p' if player bullet, 'b' if boss bullet.

	public BulletsEngine(double x, double y, double angle, char c){
		this.x=x; this.y=y; this.angle=angle; this.c=c;	
	}	

	public void increment ()
	{
		// use angle in which it was fired to increment x and y coordinate. 
		//As of now- we assume angle is straight '90 degrees' since we discussed that the player will fire bullets incessantly

		// implement increment- according to whether boss or player
		if(this.c=='b')
			if (this.x == Player.x && this.y == PLayer.y)
				ifCollision(this.x, this.y, Player.x, Player.y, 'p');

		if(this.c== 'p')
			if( this.x == Boss.x && this.x == Boss.y)
				ifCollision(this.x, this.y, Boss.x, Boss.y, 'b');
	}



	public void ifCollision(double x1, double y1, double x2, double y2, char t)
	{
		if (t == 'p')
		{
			// player was hit ITS A BOSS BULLET....DO STUFF---- CHECK FOR PLAUER HEALTH....UPDATE
			PLayer.collisionOccured();
		}

		if (t== 'b')
		{
			// boss was hit...ITS A PLAYER BULLET....DO STUFF---- CHECK FOR boss HEALTH....UPDATE

		}
	}


}	
