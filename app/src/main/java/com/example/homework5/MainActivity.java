package com.example.homework5;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.SharedPreferences;




public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    int images [] = {R.drawable.welcome, R.drawable.tiger,R.drawable.panda1,R.drawable.lion, R.drawable.giraffe, R.drawable.gorilla1};
    int currentImage;
    MediaPlayer mp;
    SoundPool sp;
    int sound_click, sound_tiger, sound_elephant, sound_lion, sound_monkey, sound_gorilla;
    int num_sounds_loaded;
    boolean sounds_loaded;
    private ImageView imageView;
    private Button previousButton, nextButton;
    private SharedPreferences settings;
    private String selected_index_key = "SELECTED_IMAGE_INDEX";
    private  int selectedIndex = 0;


    @Override
    protected void onCreate(Bundle inBundle) {
        super.onCreate(inBundle);
        setContentView(R.layout.activity_main);
        //this.settings = getPreferences(MODE_PRIVATE);

        imageView = (ImageView) findViewById(R.id.imageView);
        previousButton = (Button) findViewById(R.id.prevButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        if (inBundle != null) {
            currentImage = inBundle.getInt("currentImage");
        } else {
            currentImage = 0;
        }
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        imageView.setOnClickListener(this);

        num_sounds_loaded = 0;
        sounds_loaded = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
        }
        sp.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            num_sounds_loaded++;
            if (num_sounds_loaded == 6)
                sounds_loaded = true;
        });
        sound_click = sp.load(this, R.raw.click, 1);
        sound_tiger = sp.load(this, R.raw.tiger, 1);
        sound_elephant = sp.load(this, R.raw.elephant, 1);
        sound_lion = sp.load(this, R.raw.lion, 1);
        sound_monkey = sp.load(this, R.raw.monkey, 1);
        sound_gorilla = sp.load(this, R.raw.gorilla, 1);
    }
    @Override
    public void onClick(View v) {
        int imageCount = images.length;
        if(imageCount< 1){
            return;
        }
        switch (v.getId()){
            case R.id.prevButton: {
                if (sounds_loaded)
                    sp.play(sound_click,1,1, 0,0,1);
                if (currentImage <= 1) {
                    currentImage = imageCount - 1;
                } else {
                    currentImage -= 1;
                }
                imageView.setImageResource(images[currentImage]);
                break;
            }
            case R.id.nextButton: {
                if (currentImage >= imageCount - 1) {
                    currentImage = 1;
                } else {
                    currentImage += 1;
                }

                imageView.setImageResource(images[currentImage]);
                break;
            }
            case R.id.imageView: {
                if (sounds_loaded){
                    if (currentImage == 1)
                        sp.play(sound_tiger, 1, 1, 0, 0, 1);
                    if (currentImage == 2)
                        sp.play(sound_elephant, 1, 1, 0, 0, 1);
                    if (currentImage == 3)
                        sp.play(sound_lion, 1, 1, 0, 0, 1);
                    if (currentImage == 4)
                        sp.play(sound_monkey, 1, 1, 0, 0, 1);
                    if (currentImage == 5)
                        sp.play(sound_gorilla, 1, 1, 0, 0, 1);
                }
                break;
            }

        }
    }
    @Override
    protected void onPause() {
        if (mp!= null){
            mp.pause();
            mp.release();
            mp = null;
        }
        super.onPause();
       // SharedPreferences.Editor editor = this.settings.edit();
       // editor.putInt(selected_index_key, currentImage);
       // editor.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
       // int index = settings.getInt("SELECTED_IMAGE_INDEX", 0);
       // currentImage = index;
        imageView.setImageResource(images[currentImage]);
        mp= null;
        mp = MediaPlayer.create(this, R.raw.forest);
        if (mp != null){
            mp.setLooping(true);
            mp.start();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outBundle){
        super.onSaveInstanceState(outBundle);
        outBundle.putInt("currentImage", currentImage);
    }

}