package com.github.qiaojun2016.ms_docx

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

object FileUtil {
    /**
     * copy file
     */
    fun copyFile(src: String, dest: String) {
        FileInputStream(src).use { fis ->
            FileOutputStream(dest).use { os ->
                val buffer = ByteArray(1024)
                var len: Int
                while (fis.read(buffer).also { len = it } != -1) {
                    os.write(buffer, 0, len)
                }
                os.fd.sync();
            }
        }
    }

    /**
     * copy a stream to file
     */
    fun copyStream(inputStream: InputStream, dest: String) {
        FileOutputStream(dest).use { os ->
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                os.write(buffer, 0, len)
            }
            os.fd.sync()
        }
    }
}