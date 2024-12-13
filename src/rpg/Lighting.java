package rpg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {
	Framework fw;
	BufferedImage darknessFilter;
	
	public Lighting(Framework fw, int circleSize) {
		//Create a buffered image
		darknessFilter = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
		
		//Get the center x và y of the light circle
		int centerX = 1920/2;
		int centerY = 1080/2;
		
		//Create a gradation effect
		Color color[] = new Color[15];
		float fraction[] = new float[15];
		
		color[0] = new Color(0,0,0,0.82f);
		color[1] = new Color(0,0,0,0.76f);
		color[2] = new Color(0,0,0,0.72f);
		color[3] = new Color(0,0,0,0.65f);
		color[4] = new Color(0,0,0,0.60f);
		color[5] = new Color(0,0,0,0.54f);
		color[6] = new Color(0,0,0,0.45f);
		color[7] = new Color(0,0,0,0.35f);
		color[8] = new Color(0,0,0,0.37f);
		color[9] = new Color(0,0,0,0.45f);
		color[10] = new Color(0,0,0,0.53f);
		color[11] = new Color(0,0,0,0.58f);
		color[12] = new Color(0,0,0,0.60f);
		color[13] = new Color(0,0,0,0.60f);
		color[14] = new Color(0,0,0,0.56f);
		
		fraction[0] = 0f;
		fraction[1] = 0.2f;
		fraction[2] = 0.3f;
		fraction[3] = 0.4f;
		fraction[4] = 0.5f;
		fraction[5] = 0.55f;
		fraction[6] = 0.6f;
		fraction[7] = 0.65f;
		fraction[8] = 0.7f;
		fraction[9] = 0.75f;
		fraction[10] = 0.8f;
		fraction[11] = 0.85f;
		fraction[12] = 0.9f;
		fraction[13] = 0.95f;
		fraction[14] = 1f;
		
		// Create a gradation paint settings
		RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (circleSize/2), fraction, color);
		
		// Set the gradient data on g2
		g2.setPaint(gPaint);
		
		g2.fillRect(0, 0, 2000, 1124);

		g2.dispose();	
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(darknessFilter, 0, 0, null);
	}
}
