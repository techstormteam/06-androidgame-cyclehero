package com.techstorm.androidgame.framework;

import com.techstorm.androidgame.framework.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();

	public int getHeight();

	public PixmapFormat getFormat();

	public void dispose();
}
