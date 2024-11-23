package com.nazarov.radman.util;

import com.nazarov.radman.model.PlayingInfo;
import com.nazarov.radman.panel.PlayPanel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Metadata {

    private static final int DEFAULT_METADATA_INTERVAL = 8192;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static Future<?> future = null;
    private static InputStream stream = null;

    public static void startMetadata() {
        PlayingInfo playingInfo = PlayingInfo.getInstance();
        future = executorService.submit(() -> {
            URI streamUri = URI.create(playingInfo.getUrl().toString());
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(streamUri)
                    .header("Icy-MetaData", "1") // instruct the server, that we want the metadata
                    .build();
            client.sendAsync(request, BodyHandlers.ofInputStream())
                    .thenAccept(Metadata::accept)
                    .join();
        });
    }

    public static void stopMetadata() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (future != null) {
            future.cancel(true);
        }
    }

    private static void accept(HttpResponse<InputStream> resp) {
        HttpHeaders headers = resp.headers();
        int metaInterval = headers.firstValue("icy-metaint").map(Integer::parseInt).orElse(DEFAULT_METADATA_INTERVAL);
//TODO: think about this: String stationName = headers.firstValue("icy-name").map(String::trim).orElse("Unknown station");
        stream = resp.body();
        while (true) {
            String meta;
            try {
                stream.skipNBytes(metaInterval);
                meta = readMetaData(stream);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //meta.isEmpty - is obligatory otherwise all empty strings will be shown
            if (!meta.isEmpty() && meta.startsWith("StreamTitle")) {
                PlayPanel.setMetadata(meta.substring("StreamTitle=".length(), meta.indexOf(';')));
            }
        }
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