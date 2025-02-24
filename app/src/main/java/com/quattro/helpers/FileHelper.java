/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattro.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileHelper {


    private Context context;

    public static FileHelper getInstance() {
        return new FileHelper();
    }

    public FileHelper with(Context context) {
        this.context = context;
        return this;
    }


    public static void CreateFile(String origen, String name, Context context) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 and above
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, name);
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/x-sqlite3");
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Quattroid");

            Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            Uri itemUri = contentResolver.insert(contentUri, contentValues);
//            File destFile = new File(contentUri.toString() + "/Quattroid/" + name);
//            if (destFile.exists()) {
//                DocumentsContract.deleteDocument(contentResolver, itemUri);
//            }

            if (itemUri != null) {
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(itemUri, "rwt");
                    File origenFile = new File(origen);
                    int size = (int) origenFile.length();
                    byte[] origenBytes = new byte[size];
                    BufferedInputStream buf = new BufferedInputStream(Files.newInputStream(origenFile.toPath()));
                    buf.read(origenBytes, 0, origenBytes.length);
                    buf.close();
                    if (outputStream != null) {
                        outputStream.write(origenBytes);
                        outputStream.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Failed to create a file in the Downloads directory.");
            }
        } else {
            // Android 8 and 9
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            if (!downloadsDirectory.exists()) {
                downloadsDirectory.mkdirs();
            }
            File destFile = new File(downloadsDirectory, name);

            try {
                File origenFile = new File(origen);
                int size = (int) origenFile.length();
                byte[] origenBytes = new byte[size];
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(origenFile));
                buf.read(origenBytes, 0, origenBytes.length);
                buf.close();
                FileOutputStream outputStream = new FileOutputStream(destFile);
                outputStream.write(origenBytes);
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Get file path from uri.
     */
    private String getPathFromUri(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        String text = null;

        if (cursor.moveToNext()) {
            text = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
        }

        cursor.close();

        return text;
    }


}
