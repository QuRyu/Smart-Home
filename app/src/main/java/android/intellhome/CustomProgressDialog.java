package android.intellhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义Dialog，用于播放动画.
 * 两个构造方法分别用于播放动画和
 */
public class CustomProgressDialog extends ProgressDialog {

    static final String TAG = CustomProgressDialog.class.getSimpleName();

    static final int ANIMATION = 1;
    static final int PROGRESS = 2;

    private Context mContext;
    private String mLoadingTip;
    private int mResid;
    private DialogCallback mDialogCallback;

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mTextView;

    private boolean closed = false;
    private int category;

    
    /**
     * Constructor for ANIMATION dialog
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

        category = ANIMATION;
    }

    /**
     * Constructor for PROGRESS dialog
     * @param context
     * @param content
     * @param theme
     * @param dialogCallback
     */
    public CustomProgressDialog(Context context, String content, int theme, DialogCallback dialogCallback) {
        super(context, theme);

        this.mContext = context;
        this.mLoadingTip = content;
        this.mDialogCallback = dialogCallback;

        category = PROGRESS;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (!closed && category == PROGRESS) {
            closed = true;
            mDialogCallback.cancelAction();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        dismiss();
        return false;
    }

    /**
     * 改变TexitView显示内容
     * @param content
     */
    public void setContent(String content) {
        mTextView.setText(content);
    }

    /**
     * 关闭Dialog时调用;
     * 在dialog用于显示progress时替代dismiss()方法
     */
    public void dismissDialog() {
        if (!closed && category == PROGRESS) {
            closed = true;
            dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (category == ANIMATION) {
            initView();
            initData();
        }
        else
            setMessage(mLoadingTip);
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
