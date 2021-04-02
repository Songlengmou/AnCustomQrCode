package com.anningtex.ancustomqrcode.act;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anningtex.ancustomqrcode.MyApp;
import com.anningtex.ancustomqrcode.R;
import com.anningtex.ancustomqrcode.bean.QrMangerBean;
import com.anningtex.ancustomqrcode.camera.CameraManager;
import com.anningtex.ancustomqrcode.decoding.CaptureActivityHandler;
import com.anningtex.ancustomqrcode.decoding.InactivityTimer;
import com.anningtex.ancustomqrcode.decoding.RGBLuminanceSource;
import com.anningtex.ancustomqrcode.sql.QrMangerDatabase;
import com.anningtex.ancustomqrcode.sql.dao.QrMangerDao;
import com.anningtex.ancustomqrcode.utils.BitmapUtil;
import com.anningtex.ancustomqrcode.utils.Constant;
import com.anningtex.ancustomqrcode.utils.SavePicUtil;
import com.anningtex.ancustomqrcode.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author Song
 */
public class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final int REQUEST_CODE_SCAN_GALLERY = 100;
    private static final long VIBRATE_DURATION = 200L;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private ImageButton btnBack;
    private ImageButton btnFlash;
    private Button btnAlbum;
    private boolean isFlashOn = false;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private Bitmap scanBitmap;
    private TextView tvContent;
    private int imgSum = 1;
    private List<String> imgs = new ArrayList<>();

    private QrMangerDao qrMangerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_capture);
        initView();
    }

    private void initView() {
        viewfinderView = findViewById(R.id.viewfinder_content);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        btnFlash = findViewById(R.id.btn_flash);
        btnFlash.setOnClickListener(flashListener);
        btnAlbum = findViewById(R.id.btn_album);
        btnAlbum.setOnClickListener(albumOnClick);
        tvContent = findViewById(R.id.tv_content);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        qrMangerDao = QrMangerDatabase.getDefault(getApplicationContext()).getQrMangerDao();
        tvContent.setOnLongClickListener(view -> {
            startActivity(new Intent(CaptureActivity.this, QrCodeMangerActivity.class));
            return false;
        });
    }

    private View.OnClickListener albumOnClick = view -> {
        //打开手机中的相册
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
    };

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    handleAlbumPic(data);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 处理选择的图片
     */
    private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        final Uri uri = data.getData();
        mProgress = new ProgressDialog(CaptureActivity.this);
        mProgress.setMessage("正在扫描...");
        mProgress.setCancelable(false);
        mProgress.show();
        runOnUiThread(() -> {
            Result result = scanningImage(uri);
            mProgress.dismiss();
            if (result != null) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, result.getText());
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                showTip("识别失败");
            }
        });
    }

    /**
     * 扫描二维码图片的方法
     */
    public Result scanningImage(Uri uri) {
        if (uri == null) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        //设置二维码内容的编码
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        scanBitmap = BitmapUtil.decodeUri(this, uri, 500, 500);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        tvContent.setText("扫描结果:" + resultString);
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }

        //如果扫描到000，就归零，从头开始
        if (resultString.equals("000")) {
            imgs.clear();
            imgSum = 1;
            showTip("已归零");
            speekText("已归零");
        } else if (!imgs.contains(resultString)) {
            imgs.add(resultString);
            SavePicUtil.saveBitmap(this, barcode, resultString + ".png");
            imgSum++;
            String str = "第" + imgSum + "张";
            showTip(str);
            speekText(str);
            Log.e("666///", "SAVE_SUCCESS_PATH: " + MyApp.SAVE_SUCCESS_PATH);

            //TODO 加入数据库并且上传    "01"代表工位的手机编号(每个工位)
            QrMangerBean qrMangerBean = new QrMangerBean("01", MyApp.SAVE_SUCCESS_PATH, resultString, new Date());
            qrMangerDao.insertQrMangerBean(qrMangerBean);
        }
        Log.e("666****", "qrCode: " + resultString + ".png");
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = mediaPlayer -> mediaPlayer.seekTo(0);

    /**
     * 闪光灯开关按钮
     */
    private View.OnClickListener flashListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                boolean isSuccess = CameraManager.get().setFlashLight(!isFlashOn);
                if (!isSuccess) {
                    showTip("暂时无法开启闪光灯");
                    return;
                }
                if (isFlashOn) {
                    // 关闭闪光灯
                    btnFlash.setImageResource(R.mipmap.flash_off);
                    isFlashOn = false;
                } else {
                    // 开启闪光灯
                    btnFlash.setImageResource(R.mipmap.flash_on);
                    isFlashOn = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 语音合成（把文字转声音）
     *
     * @param str
     */
    private void speekText(String str) {
        //1. 创建 SpeechSynthesizer 对象 , 第二个参数： 本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "10");
        // 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        //设置云端
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置合成音频保存位置（可自定义保存位置），保存在 “./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(str, new MySynthesizerListener());
    }

    class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话 id，当业务出错时将会话 id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话 id为null
            //if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //     String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //     Log.d(TAG, "session id =" + sid);
            //}
        }
    }

    private void showTip(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}