package com.timetrace.example;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Write_Thread extends Thread {
    public static final String STRSAVEPATH = Environment.getExternalStorageDirectory() + "/HumanActivityData/";
    private static final float ALPHA = 0.8F;
    String nline = "\n";
    String tab = "\t";
    private String data;
    private File dir;
    private File file;
    private File file2;
    private boolean firstwrite_acc = true;
    private boolean firstwrite_pre = true;
    private float[] gravity;
    private boolean isAlive = false;
    private String phone_state = null;
    private String state;
    private String strCutTime;
    private String sub_name = "SubjectA";

    public Write_Thread(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
        this.strCutTime = paramString1;
        this.state = paramString2;
        this.data = paramString3;
        this.sub_name = paramString5;
        this.dir = makeDirectory(STRSAVEPATH + paramString2 + "/" + paramString4 + "/");
        this.file = makeFile(this.dir, STRSAVEPATH + paramString2 + "/" + paramString4 + "/" + paramString5 + paramString3 + "-" + paramString1 + ".txt");
        this.file2 = makeFile(this.dir, STRSAVEPATH + paramString2 + "/" + paramString4 + "/" + paramString5 + paramString3 + "-" + paramString1 + "al.txt");
    }

    private float[] highPass(float paramFloat1, float paramFloat2, float paramFloat3) {
        float[] arrayOfFloat = new float[3];
        this.gravity[0] = (0.8F * this.gravity[0] + 0.2F * paramFloat1);
        this.gravity[1] = (0.8F * this.gravity[1] + 0.2F * paramFloat2);
        this.gravity[2] = (0.8F * this.gravity[2] + 0.2F * paramFloat3);
        arrayOfFloat[0] = (paramFloat1 - this.gravity[0]);
        arrayOfFloat[1] = (paramFloat2 - this.gravity[1]);
        arrayOfFloat[2] = (paramFloat3 - this.gravity[2]);
        return arrayOfFloat;
    }

    private File makeDirectory(String paramString) {
        File localFile = new File(paramString);
        if (!localFile.exists())
            localFile.mkdirs();
        return localFile;
    }

    private File makeFile(File paramFile, String paramString) {
        boolean bool = paramFile.isDirectory();
        File localFile = null;
        if (bool) {
            localFile = new File(paramString);
            if ((localFile == null) || (localFile.exists())) ;
        }
        try {
            localFile.createNewFile();
            return localFile;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return localFile;
    }

    private boolean writeFile(File paramFile, String[] paramArrayOfString) {
        if ((paramFile != null) && (paramFile.exists())) ;
        while (true) {
            try {
                FileOutputStream localFileOutputStream = new FileOutputStream(paramFile, true);
                try {
                    if (!this.firstwrite_acc)
                        break;
                    localFileOutputStream.write(new String("# Raw Acceleration Values").getBytes());
                    localFileOutputStream.write(this.tab.getBytes());
                    localFileOutputStream.write(this.nline.getBytes());
                    localFileOutputStream.write(new String("# X-value, Y-value, Z-value").getBytes());
                    localFileOutputStream.write(this.tab.getBytes());
                    localFileOutputStream.write(this.nline.getBytes());
                    this.firstwrite_acc = false;
//                    break;
//                    if (i < paramArrayOfString.length)
//                        continue;
                    localFileOutputStream.write(13);
                    localFileOutputStream.write(10);
                    localFileOutputStream.flush();
                    localFileOutputStream.close();
//                    break;
//                    localFileOutputStream.write(paramArrayOfString[i].getBytes());
                    localFileOutputStream.write(this.tab.getBytes());
//                    i++;
                    continue;
                } catch (IOException localIOException) {
                    localIOException.printStackTrace();
                }
            } catch (FileNotFoundException localFileNotFoundException) {
                localFileNotFoundException.printStackTrace();
            }
            return false;
//            int i = 0;
        }
        return true;
    }

    private boolean writeFile2(File paramFile, String paramString) {
        if ((paramFile != null) && (this.file.exists()) && (paramString != null))
            try {
                FileOutputStream localFileOutputStream = new FileOutputStream(paramFile, true);
                try {
                    if (this.firstwrite_pre) {
                        localFileOutputStream.write(new String("# Raw Pressure Sensor Values").getBytes());
                        localFileOutputStream.write(this.tab.getBytes());
                        localFileOutputStream.write(this.nline.getBytes());
                        this.firstwrite_pre = false;
                    }
                    localFileOutputStream.write(paramString.getBytes());
                    localFileOutputStream.write(13);
                    localFileOutputStream.write(10);
                    localFileOutputStream.flush();
                    localFileOutputStream.close();
                    return true;
                } catch (IOException localIOException) {
                    while (true)
                        localIOException.printStackTrace();
                }
            } catch (FileNotFoundException localFileNotFoundException) {
                while (true)
                    localFileNotFoundException.printStackTrace();
            }
        return false;
    }

    public void run() {
        super.run();
        this.isAlive = true;
        if (!this.isAlive)
            return;
        if (!Info.raw_data.isEmpty()) {
            Log.d("TAG1", "size Ac" + Info.raw_data.size());
            String[] arrayOfString = (String[]) Info.raw_data.poll();
            writeFile(this.file, arrayOfString);
        }
        while (true) {
            try {
                Thread.sleep(0L);
            } catch (InterruptedException localInterruptedException) {
                localInterruptedException.printStackTrace();
            }
//            break;
            if (Info.altitude.isEmpty())
                continue;
            Log.d("TAG2", "size Al" + Info.altitude.size());
            String str = (String) Info.altitude.poll();
            writeFile2(this.file2, str);
        }
    }

    public void stopThread() {
        this.isAlive = false;
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.Write_Thread
 * JD-Core Version:    0.6.0
 */