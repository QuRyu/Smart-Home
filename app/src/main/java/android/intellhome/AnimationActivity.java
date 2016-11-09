package android.intellhome;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;

public class AnimationActivity extends AppCompatActivity {

    ImageView button;
    BlurLayout sampleLayout;
    View hover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        sampleLayout = (BlurLayout) findViewById(R.id.sampleLayout);
        hover = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_hover, null);
        sampleLayout.setHoverView(hover);
        sampleLayout.addChildAppearAnimator(hover, R.id.IV_test, Techniques.FadeIn, 550, 0);
        sampleLayout.addChildDisappearAnimator(hover, R.id.IV_test, Techniques.FadeOut, 550, 500);
        sampleLayout.setBlurDuration(100);

        button = (ImageView) findViewById(R.id.button);


    }

}
