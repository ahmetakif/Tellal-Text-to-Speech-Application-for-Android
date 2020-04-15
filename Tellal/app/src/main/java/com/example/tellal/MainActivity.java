package cf.tashimasu.tellal;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Locale;

import cf.tashimasu.tellal.R;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak;

    private String[] lang={"English/US","English/UK","Türkçe","Deutsch","Français","Italiano","РОССИЯ","日本の","한국의"};
    private Spinner spLang;
    private ArrayAdapter<String> dataAdapterForlang;

    private TextToSpeech mTTS;
    private int reklam = 1;

    AdView mAdview;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spLang = (Spinner) findViewById(R.id.spLang);
        dataAdapterForlang = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lang);
        dataAdapterForlang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLang.setAdapter(dataAdapterForlang);

        MobileAds.initialize(this, "ca-app-pub-1597449049593763~2431552336");
        mAdview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1597449049593763/5843225876");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });

        mButtonSpeak = (Button) findViewById(R.id.button_speak);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mSeekBarPitch = (SeekBar) findViewById(R.id.seek_bar_pitch);
        mSeekBarSpeed = (SeekBar) findViewById(R.id.seek_bar_speed);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("tr", "TR"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Maalesef bu dil desteklenemiyor");
                    }
                }
                else {
                    Log.e("TTS", "Baslatma basarisiz oldu");
                }
            }
        });

        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = spLang.getSelectedItem().toString();
                TextView newtext;
                newtext = (TextView) findViewById(R.id.textView3);
                newtext.setText(language);
                if (language == "English/US") {
                    mTTS.setLanguage(new Locale("en", "US"));
                }
                else if (language == "English/UK") {
                    mTTS.setLanguage(new Locale("en", "GB"));
                }
                else if (language == "Türkçe") {
                    mTTS.setLanguage(new Locale("tr", "TR"));
                }
                else if (language == "Deutsch") {
                    mTTS.setLanguage(new Locale("de", "DE"));
                }
                else if (language == "Français") {
                    mTTS.setLanguage(new Locale("fr", "FR"));
                }
                else if (language == "Italiano") {
                    mTTS.setLanguage(new Locale("it", "IT"));
                }
                else if (language == "РОССИЯ") {
                    mTTS.setLanguage(new Locale("ru", "RU"));
                }
                else if (language == "日本の") {
                    mTTS.setLanguage(new Locale("ja", "JP"));
                }
                else if (language == "한국의") {
                    mTTS.setLanguage(new Locale("ko", "KR"));
                }
                reklam = reklam + 1;
                String text = mEditText.getText().toString();
                float pitch = (float) mSeekBarPitch.getProgress() / 50;
                if (pitch < 0.1) pitch = 0.1f;
                float speed = (float) mSeekBarSpeed.getProgress() / 50;
                if (speed < 0.1) speed = 0.1f;
                mTTS.setPitch(pitch);
                mTTS.setSpeechRate(speed);
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                int test = reklam/5;
                if (test == (int)test && reklam > 6) {
                    showInterstitial();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    public void showInterstitial() {
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitial();
    }
}
