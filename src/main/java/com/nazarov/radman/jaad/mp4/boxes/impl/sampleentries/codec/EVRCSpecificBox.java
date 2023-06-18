package com.nazarov.radman.jaad.mp4.boxes.impl.sampleentries.codec;

import com.nazarov.radman.jaad.mp4.MP4InputStream;

import java.io.IOException;

public class EVRCSpecificBox extends CodecSpecificBox {

	private int framesPerSample;

	public EVRCSpecificBox() {
		super("EVCR Specific Box");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		decodeCommon(in);

		framesPerSample = in.read();
	}

	public int getFramesPerSample() {
		return framesPerSample;
	}
}
