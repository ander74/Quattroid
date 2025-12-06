/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Helpers;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {

    private static final String TAG = "FileHelper";
    public static final String DATABASE_NAME = "Quattro";

    /**
     * !! CAVEAT: Directly writing to Documents folder using a path is NOT reliable
     * on API 29+ due to Scoped Storage. Use SAF and URIs instead. !!
     * This method is provided for illustration or legacy scenarios.
     * <p>
     * Copies the app's internal database to a specified destination file path.
     *
     * @param context            The application context.
     * @param destinationPathStr The full path string for the destination file.
     * @return true if successful, false otherwise.
     */
    @OptIn(markerClass = UnstableApi.class)
    public static boolean exportDatabaseToPath(Context context, String destinationPathStr) {
        File dbFileInternal = context.getDatabasePath(DATABASE_NAME);
        if (!dbFileInternal.exists()) {
            Log.e(TAG, "Database file does not exist: " + dbFileInternal.getAbsolutePath());
            return false;
        }

        File destinationFile = new File(destinationPathStr);

        // Ensure the destination directory exists
        File parentDir = destinationFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                Log.w(TAG, "Could not create destination directory (might fail due to permissions): " + parentDir.getAbsolutePath());
                // Don't immediately return false, attempt the copy anyway, but log a warning.
            }
        }

        try (InputStream in = new FileInputStream(dbFileInternal);
             OutputStream out = new FileOutputStream(destinationFile, false)) { // false to overwrite

            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            Log.i(TAG, "Database exported successfully to: " + destinationPathStr);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error exporting database to path '" + destinationPathStr + "': " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * !! CAVEAT: Directly reading from Documents folder using a path is NOT reliable
     * on API 29+ due to Scoped Storage. Use SAF and URIs instead. !!
     * This method is provided for illustration or legacy scenarios.
     * <p>
     * Copies a database file from a source file path to the app's internal database location.
     *
     * @param context       The application context.
     * @param sourcePathStr The full path string of the source database file.
     * @return true if successful, false otherwise.
     */
    @OptIn(markerClass = UnstableApi.class)
    public static boolean importDatabaseFromPath(Context context, String sourcePathStr) {
        File sourceFile = new File(sourcePathStr);
        if (!sourceFile.exists()) {
            Log.e(TAG, "Source database file does not exist: " + sourcePathStr);
            return false;
        }
        if (!sourceFile.canRead()) {
            Log.e(TAG, "Cannot read source database file (check permissions): " + sourcePathStr);
            return false;
        }


        File dbFileInternal = context.getDatabasePath(DATABASE_NAME);

        File dbDir = dbFileInternal.getParentFile();
        if (dbDir != null && !dbDir.exists()) {
            if (!dbDir.mkdirs()) {
                Log.e(TAG, "Could not create internal database directory: " + dbDir.getAbsolutePath());
                return false;
            }
        }

        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(dbFileInternal, false)) { // false to overwrite

            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            Log.i(TAG, "Database imported successfully from: " + sourcePathStr + " to: " + dbFileInternal.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error importing database from path '" + sourcePathStr + "': " + e.getMessage(), e);
            return false;
        }
    }


    /**
     * !! CAVEAT: This method is deprecated on API 29+ for public directories.
     * Use with extreme caution and expect it to fail on newer Android versions
     * for accessing Documents folder without SAF or special permissions. !!
     *
     * @return A File object pointing to the public Documents directory, or null if not available/accessible.
     */
    @OptIn(markerClass = UnstableApi.class)
    @SuppressWarnings("deprecation")
    public static File getPublicDocumentsStorageDir() {
        // Attempt to get the directory. This is deprecated and unreliable for general access.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (file == null) {
            Log.e(TAG, "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) returned null.");
            return null;
        }
        // Even if a path is returned, writing to it might not be permitted.
        // For backup purposes, consider creating a sub-folder for your app.
        File appBackupDir = new File(file, "YourAppBackups"); // Or your app name
        if (!appBackupDir.exists()) {
            if (!appBackupDir.mkdirs()) {
                Log.w(TAG, "Could not create app-specific backup directory in Documents (permissions?): " + appBackupDir.getAbsolutePath());
                // Fallback to the root Documents directory if sub-folder creation fails, but this is less ideal.
                return file;
            }
        }
        return appBackupDir;
    }
}
