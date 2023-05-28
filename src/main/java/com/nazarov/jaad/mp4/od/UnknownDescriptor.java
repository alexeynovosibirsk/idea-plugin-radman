package com.nazarov.jaad.mp4.od;

import com.nazarov.jaad.mp4.MP4InputStream;

import java.io.IOException;

/**
 * This class is used if any unknown Descriptor is found in a stream. All
 * contents of the Descriptor will be skipped.
 *
 * @author in-somnia
 */
public class UnknownDescriptor extends Descriptor {

	@Override
	void decode(MP4InputStream in) throws IOException {
		//content will be skipped
	}
}
