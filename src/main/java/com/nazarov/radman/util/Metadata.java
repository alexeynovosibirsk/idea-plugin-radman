package com.nazarov.radman.util;

import com.nazarov.radman.model.PlayingInfo;
import com.nazarov.radman.panel.PlayPanel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

public class Metadata extends Thread {

    private static final int DEFAULT_METADATA_INTERVAL = 8192;
    //TODO: Use ExecutorService
    private Thread thread;
    private static Metadata INSTANCE = null;
    private boolean isMetadata;

    private Metadata() {
    }

    public static Metadata getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Metadata();
        }
        return INSTANCE;
    }

    public void startMetadata() {
        isMetadata = true;
        thread = new Metadata();
        thread.start();
    }

    public void stopMetadata() {
        if (isMetadata) {
            isMetadata = false;
            thread.stop();
        }
    }

    private final PlayingInfo playingInfo = PlayingInfo.getInstance();

    @Override
    public void run() {

        URI streamUri = URI.create(playingInfo.getUrl().toString());

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(streamUri)
                .header("Icy-MetaData", "1") // instruct the server, that we want the metadata
                .build();
        client.sendAsync(request, BodyHandlers.ofInputStream())
                .thenAccept(resp -> {
                    HttpHeaders headers = resp.headers();
                    int metaInterval = headers.firstValue("icy-metaint").map(Integer::parseInt).orElse(DEFAULT_METADATA_INTERVAL);

                    try (InputStream stream = resp.body()) {
                        while (true) {
                            stream.skipNBytes(metaInterval);
                            String meta = readMetaData(stream);
                            if (!meta.isEmpty() && meta.startsWith("StreamTitle")) {

                                PlayPanel.setMetadata(meta.substring("StreamTitle=".length(), meta.indexOf(';')));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .join();
    }

    private static String readMetaData(InputStream stream) throws IOException {
        // The next byte after a music chunk indicates the length of the metadata
        int length = stream.read();
        if (length < 1) return ""; // no new metadata is present (the song is playing)

        // Multiply by 16 to get the number of bytes, that holds the data
        int metaChunkSize = length * 16;
        byte[] metaChunk = stream.readNBytes(metaChunkSize);
        return new String(metaChunk, 0, metaChunkSize, StandardCharsets.ISO_8859_1);
    }

}