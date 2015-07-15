package com.timetrace.example;

import android.media.AudioRecord;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Recorder {
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "HumanActivityData";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_AUDIO_ENCODING = 2;
    private static final int RECORDER_BPP = 16;
    private static final int RECORDER_CHANNELS = 12;
    private static final int RECORDER_SAMPLERATE = 44100;
    String currdate = null;
    private int bufferSize = 0;
    private boolean isRecording = false;
    private String phone_state = null;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private String state;
    private String sub_name = "SubjectA";

    public Recorder(String paramString1, String paramString2, String paramString3) {
        this.state = paramString1;
        this.phone_state = paramString2;
        this.sub_name = paramString3;
        this.bufferSize = AudioRecord.getMinBufferSize(44100, 12, 2);
    }

    private void WriteWaveFileHeader(FileOutputStream paramFileOutputStream, long paramLong1, long paramLong2, long paramLong3, int paramInt, long paramLong4)
            throws IOException {
        byte[] arrayOfByte = new byte[44];
        arrayOfByte[0] = 82;
        arrayOfByte[1] = 73;
        arrayOfByte[2] = 70;
        arrayOfByte[3] = 70;
        arrayOfByte[4] = (byte) (int) (0xFF & paramLong2);
        arrayOfByte[5] = (byte) (int) (0xFF & paramLong2 >> 8);
        arrayOfByte[6] = (byte) (int) (0xFF & paramLong2 >> 16);
        arrayOfByte[7] = (byte) (int) (0xFF & paramLong2 >> 24);
        arrayOfByte[8] = 87;
        arrayOfByte[9] = 65;
        arrayOfByte[10] = 86;
        arrayOfByte[11] = 69;
        arrayOfByte[12] = 102;
        arrayOfByte[13] = 109;
        arrayOfByte[14] = 116;
        arrayOfByte[15] = 32;
        arrayOfByte[16] = 16;
        arrayOfByte[17] = 0;
        arrayOfByte[18] = 0;
        arrayOfByte[19] = 0;
        arrayOfByte[20] = 1;
        arrayOfByte[21] = 0;
        arrayOfByte[22] = (byte) paramInt;
        arrayOfByte[23] = 0;
        arrayOfByte[24] = (byte) (int) (0xFF & paramLong3);
        arrayOfByte[25] = (byte) (int) (0xFF & paramLong3 >> 8);
        arrayOfByte[26] = (byte) (int) (0xFF & paramLong3 >> 16);
        arrayOfByte[27] = (byte) (int) (0xFF & paramLong3 >> 24);
        arrayOfByte[28] = (byte) (int) (0xFF & paramLong4);
        arrayOfByte[29] = (byte) (int) (0xFF & paramLong4 >> 8);
        arrayOfByte[30] = (byte) (int) (0xFF & paramLong4 >> 16);
        arrayOfByte[31] = (byte) (int) (0xFF & paramLong4 >> 24);
        arrayOfByte[32] = 4;
        arrayOfByte[33] = 0;
        arrayOfByte[34] = 16;
        arrayOfByte[35] = 0;
        arrayOfByte[36] = 100;
        arrayOfByte[37] = 97;
        arrayOfByte[38] = 116;
        arrayOfByte[39] = 97;
        arrayOfByte[40] = (byte) (int) (0xFF & paramLong1);
        arrayOfByte[41] = (byte) (int) (0xFF & paramLong1 >> 8);
        arrayOfByte[42] = (byte) (int) (0xFF & paramLong1 >> 16);
        arrayOfByte[43] = (byte) (int) (0xFF & paramLong1 >> 24);
        paramFileOutputStream.write(arrayOfByte, 0, 44);
    }

    // ERROR //
    private void copyWaveFile(String paramString1, String paramString2) {
        // Byte code:
        //   0: lconst_0
        //   1: ldc2_w 85
        //   4: ladd
        //   5: pop2
        //   6: ldc 87
        //   8: i2l
        //   9: lstore 5
        //   11: aload_0
        //   12: getfield 43	com/example/monitoringactivity/Recorder:bufferSize	I
        //   15: newarray byte
        //   17: astore 7
        //   19: new 89	java/io/FileInputStream
        //   22: dup
        //   23: aload_1
        //   24: invokespecial 92	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
        //   27: astore 8
        //   29: new 71	java/io/FileOutputStream
        //   32: dup
        //   33: aload_2
        //   34: invokespecial 93	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
        //   37: astore 9
        //   39: aload 8
        //   41: invokevirtual 97	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
        //   44: invokevirtual 103	java/nio/channels/FileChannel:size	()J
        //   47: lstore 12
        //   49: aload_0
        //   50: aload 9
        //   52: lload 12
        //   54: lload 12
        //   56: ldc2_w 85
        //   59: ladd
        //   60: ldc2_w 104
        //   63: iconst_2
        //   64: lload 5
        //   66: invokespecial 107	com/example/monitoringactivity/Recorder:WriteWaveFileHeader	(Ljava/io/FileOutputStream;JJJIJ)V
        //   69: aload 8
        //   71: aload 7
        //   73: invokevirtual 111	java/io/FileInputStream:read	([B)I
        //   76: iconst_m1
        //   77: if_icmpne +14 -> 91
        //   80: aload 8
        //   82: invokevirtual 114	java/io/FileInputStream:close	()V
        //   85: aload 9
        //   87: invokevirtual 115	java/io/FileOutputStream:close	()V
        //   90: return
        //   91: aload 9
        //   93: aload 7
        //   95: invokevirtual 118	java/io/FileOutputStream:write	([B)V
        //   98: goto -29 -> 69
        //   101: astore 11
        //   103: aload 11
        //   105: invokevirtual 121	java/io/FileNotFoundException:printStackTrace	()V
        //   108: return
        //   109: astore 10
        //   111: aload 10
        //   113: invokevirtual 122	java/io/IOException:printStackTrace	()V
        //   116: return
        //   117: astore 10
        //   119: goto -8 -> 111
        //   122: astore 10
        //   124: goto -13 -> 111
        //   127: astore 11
        //   129: goto -26 -> 103
        //   132: astore 11
        //   134: goto -31 -> 103
        //
        // Exception table:
        //   from	to	target	type
        //   39	69	101	java/io/FileNotFoundException
        //   69	90	101	java/io/FileNotFoundException
        //   91	98	101	java/io/FileNotFoundException
        //   19	29	109	java/io/IOException
        //   29	39	117	java/io/IOException
        //   39	69	122	java/io/IOException
        //   69	90	122	java/io/IOException
        //   91	98	122	java/io/IOException
        //   19	29	127	java/io/FileNotFoundException
        //   29	39	132	java/io/FileNotFoundException
    }

    private void deleteTempFile() {
        new File(getTempFilename()).delete();
    }

    private String getFilename() {
        File localFile = new File(Environment.getExternalStorageDirectory().getPath(), "HumanActivityData");
        if (!localFile.exists())
            localFile.mkdirs();
        return localFile.getAbsolutePath() + "/" + this.state + "/" + this.phone_state + "/" + this.sub_name + "-wav-audio-" + this.currdate + ".wav";
    }

    private String getTempFilename() {
        String str = Environment.getExternalStorageDirectory().getPath();
        File localFile1 = new File(str, "HumanActivityData");
        if (!localFile1.exists())
            localFile1.mkdirs();
        File localFile2 = new File(str, "record_temp.raw");
        if (localFile2.exists())
            localFile2.delete();
        return localFile1.getAbsolutePath() + "/" + this.state + "/" + this.phone_state + "/" + this.sub_name + "-raw-audio-" + this.currdate + "record_temp.raw";
    }

    private void writeAudioDataToFile() {
        byte[] arrayOfByte = new byte[this.bufferSize];
        String str = getTempFilename();
        FileOutputStream localFileOutputStream2;

        try {
            FileOutputStream localFileOutputStream1 = new FileOutputStream(str);
            localFileOutputStream2 = localFileOutputStream1;
//            if (localFileOutputStream2 != null)
//                if (this.isRecording)
//                    break;
        } catch (FileNotFoundException localFileNotFoundException) {
//            try {
            while (true) {
//                    localFileOutputStream2.close();
//                    return;
                localFileNotFoundException = localFileNotFoundException;
                localFileNotFoundException.printStackTrace();
                localFileOutputStream2 = null;
//                    continue;
//                    if (-3 == this.recorder.read(arrayOfByte, 0, this.bufferSize))
//                        continue;
//                    try {
//                        localFileOutputStream2.write(arrayOfByte);
//                    } catch (IOException localIOException1) {
//                        localIOException1.printStackTrace();
//                    }
//                }
//            }
//            catch (IOException localIOException2) {
//                localIOException2.printStackTrace();
            }
        }
    }

    public void setDateTime(Date paramDate) {
        this.currdate = new SimpleDateFormat("MM-dd-HH:mm:ss", Locale.KOREA).format(paramDate);
    }

    public void startRecording() {
        this.recorder = new AudioRecord(1, 44100, 12, 2, this.bufferSize);
        this.recorder.startRecording();
        this.isRecording = true;
        this.recordingThread = new Thread(new Runnable() {
            public void run() {
                Recorder.this.writeAudioDataToFile();
            }
        }
                , "AudioRecorder Thread");
        this.recordingThread.start();
    }

    public void stopRecording() {
        if (this.recorder != null) {
            this.isRecording = false;
            this.recorder.stop();
            this.recorder.release();
            this.recorder = null;
            this.recordingThread = null;
        }
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.Recorder
 * JD-Core Version:    0.6.0
 */