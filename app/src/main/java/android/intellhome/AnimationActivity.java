package android.intellhome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AnimationActivity extends AppCompatActivity {

    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        button = (ImageView) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CustomProgressDialog dialog = new CustomProgressDialog(
//                        AnimationActivity.this, "已启动", R.drawable.launcher, R.style.dialog_animation);
                CustomProgressDialog dialog = new CustomProgressDialog(AnimationActivity.this, "正在加载...", R.style.dialog_progress,
                        new DialogCallback() {
                            @Override
                            public void cancelAction() {
                                Toast.makeText(AnimationActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.show();
//                dialog.show();
            }
        });
    }


}
