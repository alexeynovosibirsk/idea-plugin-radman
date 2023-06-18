package com.nazarov.radman.jaad.aac.syntax;

import com.nazarov.radman.jaad.aac.AACException;
import com.nazarov.radman.jaad.aac.SampleFrequency;
import com.nazarov.radman.jaad.aac.sbr.SBR;

public abstract class Element implements Constants {

	private int elementInstanceTag;
	private SBR sbr;

	protected void readElementInstanceTag(BitStream in) throws AACException {
		elementInstanceTag = in.readBits(4);
	}

	public int getElementInstanceTag() {
		return elementInstanceTag;
	}

	void decodeSBR(BitStream in, SampleFrequency sf, int count, boolean stereo, boolean crc, boolean downSampled,boolean smallFrames) throws AACException {
		if(sbr==null) {
            /* implicit SBR signalling, see 4.6.18.2.6 */
			int fq = sf.getFrequency();
			if(fq<24000 && !downSampled)
			    sf = SampleFrequency.forFrequency(2*fq);
			sbr = new SBR(smallFrames, stereo, sf, downSampled);
		}
		sbr.decode(in, count, crc);
	}

	boolean isSBRPresent() {
		return sbr!=null;
	}

	SBR getSBR() {
		return sbr;
	}
}
