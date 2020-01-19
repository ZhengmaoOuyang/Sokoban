import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Walls {
	
	
	String path = "/brick.jpg";
	BufferedImage img;
	BufferedImage img2;
	int xPixel;
	int yPixel;
	double scaleFactor;
	
	public Walls(int x, int y, int tileSideLength, int hOffset, int vOffset) {

		setXPixel(hOffset + x*tileSideLength);
		setYPixel(vOffset + y*tileSideLength);
		
		try {
			img = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        scaleFactor = ((double)tileSideLength)/(img.getWidth());
		
		img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform scaleImg = new AffineTransform();
		scaleImg.scale(scaleFactor, scaleFactor);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(scaleImg, AffineTransformOp.TYPE_BILINEAR);
		img2 = scaleOp.filter(img, img2);
		
	}
	
	public int getXPixel() {
		
		return xPixel;
		
	}
	
	public int getYPixel() {
		
		return yPixel;
		
	}
	
	public void setXPixel(int x) {
		xPixel = x;
	}
	
	public void setYPixel(int y) {
		
		yPixel = y;
		
	}
	
	
	public BufferedImage getBufferedImage() {
		
		return img2;
		
	}
	
	
}