package com.nazarov.jaad.mp4.boxes.impl;

import com.nazarov.jaad.mp4.MP4InputStream;
import com.nazarov.jaad.mp4.boxes.BoxImpl;

import java.io.IOException;

public class PixelAspectRatioBox extends BoxImpl {

	private long hSpacing;
	private long vSpacing;

	public PixelAspectRatioBox() {
		super("Pixel Aspect Ratio Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		hSpacing = in.readBytes(4);
		vSpacing = in.readBytes(4);
	}

	public long getHorizontalSpacing() {
		return hSpacing;
	}

	public long getVerticalSpacing() {
		return vSpacing;
	}
}
