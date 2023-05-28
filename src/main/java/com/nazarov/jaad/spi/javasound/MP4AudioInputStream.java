package com.nazarov.jaad.spi.javasound;

import com.nazarov.jaad.aac.Decoder;
import com.nazarov.jaad.aac.SampleBuffer;
import com.nazarov.jaad.mp4.MP4Container;
import com.nazarov.jaad.mp4.api.AudioTrack;
import com.nazarov.jaad.mp4.api.Frame;
import com.nazarov.jaad.mp4.api.Movie;
import com.nazarov.jaad.mp4.api.Track;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class MP4AudioInputStream extends AsynchronousAudioInputStream {

	private final AudioTrack track;
	private final Decoder decoder;
	private final SampleBuffer sampleBuffer;
	private AudioFormat audioFormat;
	private byte[] saved;

	static final String ERROR_MESSAGE_AAC_TRACK_NOT_FOUND = "movie does not contain any AAC track";

	MP4AudioInputStream(InputStream in, AudioFormat format, long length) throws IOException {
		super(in, format, length);
		final MP4Container cont = new MP4Container(in);
		final Movie movie = cont.getMovie();
		final List<Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
		if(tracks.isEmpty()) throw new IOException(ERROR_MESSAGE_AAC_TRACK_NOT_FOUND);
		track = (AudioTrack) tracks.get(0);

		decoder = new Decoder(track.getDecoderSpecificInfo());
		sampleBuffer = new SampleBuffer();
	}

	@Override
	public AudioFormat getFormat() {
		if(audioFormat==null) {
			//read first frame
			decodeFrame();
			audioFormat = new AudioFormat(sampleBuffer.getSampleRate(), sampleBuffer.getBitsPerSample(), sampleBuffer.getChannels(), true, true);
			saved = sampleBuffer.getData();
		}
		return audioFormat;
	}

	public void execute() {
		if(saved==null) {
			decodeFrame();
			if(buffer.isOpen()) buffer.write(sampleBuffer.getData());
		}
		else {
			buffer.write(saved);
			saved = null;
		}
	}

	private void decodeFrame() {
		if(!track.hasMoreFrames()) {
			buffer.close();
			return;
		}
		try {
			final Frame frame = track.readNextFrame();
			if(frame==null) {
				buffer.close();
				return;
			}
			decoder.decodeFrame(frame.getData(), sampleBuffer);
		}
		catch(IOException e) {
			buffer.close();
			return;
		}
	}
}
