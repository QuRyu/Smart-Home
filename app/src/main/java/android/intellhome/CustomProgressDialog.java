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
 * Created by Quentin on 09/11/2016.
 */
public class CustomProgressDialog extends ProgressDialog {

    static final String TAG = CustomProgressDialog.class.getSimpleName();

    private Context mContext;
    private String mLoadingTip;
    private int mResid;

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mTextView;

    private int count = 0;

    public CustomProgressDialog(Context context, String content, int id, int theme) {
        super(context, theme);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = id;
    }

    public void setContent(String content) {
        if (mTextView != null)
            mTextView.setText(content);
        else
            throw new RuntimeException("TextView is not initialized");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.animation_dialog);
        mTextView = (TextView) findViewById(R.id.tv_dialog);
        mImageView = (ImageView) findViewById(R.id.iv_dialog);
    }

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
