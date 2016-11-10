package android.intellhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * 自定义Dialog，用于播放动画.
 */
public class CustomProgressDialog extends ProgressDialog {

    static final String TAG = CustomProgressDialog.class.getSimpleName();

    private Context mContext;
    private String mLoadingTip;
    private int mResid;

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mTextView;
    
    /**
     * Constructor.
     * @param context
     * @param content TextView 显示内容
     * @param id 播放动画文件id
     * @param theme Dialog style id
     */
    public CustomProgressDialog(Context context, String content, int id, int theme) {
        super(context, theme);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = id;
    }

    /**
     * 改变TexitView显示内容
     * @param content
     */
    public void setContent(String content) {
        mTextView.setText(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 初始化View
     */
    private void initView() {
        setContentView(R.layout.animation_dialog);
        mTextView = (TextView) findViewById(R.id.tv_dialog);
        mImageView = (ImageView) findViewById(R.id.iv_dialog);
    }

    /**
     * 初始化View所显示内容，并开始播放动画
     */
    private void initData() {
        mImageView.setBackgroundResource(mResid);
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });

        mTextView.setText(mLoadingTip);
        Log.i(TAG, "initData: size of image view - " + mImageView.getHeight() + "  " + mImageView.getWidth());
    }


}
