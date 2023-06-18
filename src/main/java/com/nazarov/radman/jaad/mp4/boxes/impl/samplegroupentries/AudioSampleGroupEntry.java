package com.nazarov.radman.jaad.mp4.boxes.impl.samplegroupentries;

import com.nazarov.radman.jaad.mp4.MP4InputStream;

import java.io.IOException;

public class AudioSampleGroupEntry extends SampleGroupDescriptionEntry {

	public AudioSampleGroupEntry() {
		super("Audio Sample Group Entry");
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
	}
}
