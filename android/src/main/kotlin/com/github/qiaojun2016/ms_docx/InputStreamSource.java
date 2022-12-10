package com.github.qiaojun2016.ms_docx;


import android.content.Context;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource {

    private static final int BUFFER_SIZE = 32 * 1024;
    private static final String ERROR_UNSUPPORTED_SCHEME = "Unsupported file source path = %s";

    InputStream getStream(Context context, String fileUri) throws IOException {
        switch (SourceScheme.ofUri(fileUri)) {

            case FILE:
                return getStreamFromFile(fileUri);

            case ASSETS:
                return getStreamFromAssets(context, fileUri);

            case UNKNOWN:
            default:
                return getStreamFromOtherSource(fileUri);
        }
    }

    private InputStream getStreamFromFile(String fileUri) throws IOException {
        String filePath = SourceScheme.FILE.crop(fileUri);
        return new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
    }

    private InputStream getStreamFromAssets(Context context, String fileUri) throws IOException {
        String filePath = SourceScheme.ASSETS.crop(fileUri);
        return context.getAssets().open(filePath);
    }

    private InputStream getStreamFromOtherSource(String fileUri) throws IOException {
        throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, fileUri));
    }

}