/**
 * 
 */
package com.github.jtse.puzzle.ogl;

import java.util.Arrays;

import org.newdawn.slick.opengl.Texture;

/**
 * Creates an region for a bitmap image, using the non-zero alpha values. 
 * This class can be used for collision detection.
 * 
 * @author jtse
 *
 */
public final class Region {
	private int x;
	private int y;
	private final int width;
	private final int height;
	private final boolean[] data;	// true means that there's a region

	/**
	 * @param width
	 * @param height
	 * @param data array of booleans. Note that boolean data is copied.
	 */
	public Region(int width, int height, boolean[] data) {
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		this.data = Arrays.copyOf(data, data.length);
	}
	
	
	/**
	 * @return the x
	 */
	public final int getX() {
		return x;
	}


	/**
	 * @param x the x to set
	 */
	public final void setX(int x) {
		this.x = x;
	}


	/**
	 * @return the y
	 */
	public final int getY() {
		return y;
	}


	/**
	 * @param y the y to set
	 */
	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * Sets the delta x and delta y, useful for moving
	 * @param dx
	 * @param dy
	 */
	public final void setDxDy(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	/**
	 * Sets the region position to x,y
	 * @param x
	 * @param y
	 */
	public final void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final int getHeight() {
		return height;
	}
	
	/**
	 * Determines if region contains x and y
	 * @param x
	 * @param y
	 * @return true of x and y inside the region
	 */
	public final boolean contains(int x, int y) {
		if (this.x <= x && (this.x + this.width) > x && this.y <= y && (this.y + this.height) > y) {
			int i = (y - this.y) * width + (x - this.x);
			return i > -1 && i < data.length && data[i];
		}
		return false;
	}
	
	/**
	 * This can be used for collision detection
	 * @param region
	 * @return true if regions intersect
	 */
	public final boolean intersects(Region region) {
		for(int i = 0; i < data.length; i++) {
			if (data[i]) {
				int x = this.x + (i % width);
				int y = this.y + (i / width);
				if (region.contains(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Constructs a region given a Texture
	 * @param texture
	 * @return Region
	 */
	public static final Region createRegion(Texture texture) {
		int imgWidth = texture.getImageWidth();
		int imgHeight = texture.getImageHeight();
		
		int width = texture.getTextureWidth();
		int height = texture.getTextureHeight();
		
		byte[] bytes = texture.getTextureData();
		boolean[] data = new boolean[width * height];
		
		for(int i = 3, j = 0; i < bytes.length; i += 4, j++) {
			// TODO clean out residual right edge
			int x = j % width;
			int y = j / width;

			int alpha = (int)bytes[i] & 0xFF;
			data[j] = alpha > 128 && x < imgWidth && y < imgHeight;
		}

		return new Region(width, height, data);
	}
	
	/**
	 * Creates a block region that can be used like a wall
	 * @param width
	 * @param height
	 * @return
	 */
	public static final Region createBlock(final int width, final int height) {
		final boolean[] data = new boolean[width * height];
		for(int i = 0; i < data.length; i++) {
			data[i] = true;
		}
		
		return new Region(width, height, data);
	}

	/**
	 * Same as createBlock(width, height) but with initial x,y set
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 * @return
	 */
	public static final Region createBlock(final int width, final int height, final int x, final int y) {
		Region region = createBlock(width, height);
		region.setXY(x, y);
		return region;
	}
}
