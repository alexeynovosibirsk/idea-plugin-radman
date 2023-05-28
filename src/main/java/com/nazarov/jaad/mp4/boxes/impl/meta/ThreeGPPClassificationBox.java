package com.nazarov.jaad.mp4.boxes.impl.meta;

import com.nazarov.jaad.mp4.MP4InputStream;

import java.io.IOException;

public class ThreeGPPClassificationBox extends ThreeGPPMetadataBox {

	private long entity;
	private int table;

	public ThreeGPPClassificationBox() {
		super("3GPP Classification Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		decodeCommon(in);

		entity = in.readBytes(4);
		table = (int) in.readBytes(2);
	}

	public long getEntity() {
		return entity;
	}

	public int getTable() {
		return table;
	}
}
