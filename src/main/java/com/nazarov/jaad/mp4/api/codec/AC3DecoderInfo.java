package com.nazarov.jaad.mp4.api.codec;

import com.nazarov.jaad.mp4.api.DecoderInfo;
import com.nazarov.jaad.mp4.boxes.impl.sampleentries.codec.AC3SpecificBox;
import com.nazarov.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AC3DecoderInfo extends DecoderInfo {

	private AC3SpecificBox box;

	public AC3DecoderInfo(CodecSpecificBox box) {
		this.box = (AC3SpecificBox) box;
	}

	public boolean isLfeon() {
		return box.isLfeon();
	}

	public int getFscod() {
		return box.getFscod();
	}

	public int getBsmod() {
		return box.getBsmod();
	}

	public int getBsid() {
		return box.getBsid();
	}

	public int getBitRateCode() {
		return box.getBitRateCode();
	}

	public int getAcmod() {
		return box.getAcmod();
	}
}
