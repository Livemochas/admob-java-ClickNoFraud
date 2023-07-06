package com.example.clicknofraud;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView TextViews;

    private static final String TAG = "MyActivity";
    private InterstitialAd interstitialAd;

    //CLICKNOFRAUD: VARIAVEL ARQUIVO XML QUE SALVA OS DADOS E OUTRAS
    private static final  String DB_XML = "NOME_DB_APP"; // nome do banco de dados
    private static final int carrementoLimite =  3; // limite de carregamentos
    private static final int clickLimite = 3; // limite de clicks
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    int carregamento;
    int click;
    int GetClick;
    String GetData;
    String Data;
    //CLICKNOFRAUD: VARIAVEL ARQUIVO XML QUE SALVA OS DADOS E OUTRAS

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt", "SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CLICKNOFRAUD: RECUPERA AS DATAS E CHAMA O EDITE
        preferences = getSharedPreferences(DB_XML,0);
        editor = preferences.edit();
        GetClick = preferences.getInt("GetClick", 0);
        GetData = preferences.getString("GetData", "00/00");
        //CLICKNOFRAUD: RECUPERA AS DATAS E CHAMA O EDITE

        // funçao para iniciar o sdk do admob
        MobileAds.initialize(this, initializationStatus -> {
        });
        loadAd();

        TextViews    = (TextView) findViewById(R.id.TextView);
        TextViews.setText("GetClick: " + GetClick + " GetDatas: " + GetData);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                VezesProcessada();

            }
        });

    }
    //CLICKNOFRAUD: METODOS DE VEZESPROCESSADAS, CLICKFRAUD, SALVADATAS
    private void VezesProcessada() {
        carregamento += 1;
        if (carregamento >= carrementoLimite && GetClick <= clickLimite) {
            showInterstitial();
            carregamento = 0;
        }else if(GetClick > clickLimite){
            ResetClicks();
        }
    }
    public void ClickNoFraud(){
        Date dataHoraAtual = new Date();
        Data = new SimpleDateFormat("dd/MM").format(dataHoraAtual);
        click = click +1;
        editor.putInt("GetClick", click);
        editor.putString("GetData", Data);
        editor.commit();
        GetClick = preferences.getInt("GetClick", 0);
        GetData = preferences.getString("GetData", "00/00");
        TextViews    = (TextView) findViewById(R.id.TextView);
        TextViews.setText("GetClick: " + GetClick + " GetDatas: " + GetData);
    }
    public void ResetClicks(){
        Date dataHoraAtual = new Date();
        Data = new SimpleDateFormat("dd/MM").format(dataHoraAtual);
        GetData = preferences.getString("GetData", "00/00");
        if(!Objects.equals(GetData, Data)){
            click = 0;
            Data = "00/00";
            editor.putInt("GetClick", click);
            editor.putString("GetData", Data);
            editor.commit();
            GetClick = preferences.getInt("GetClick", 0);
            GetData = preferences.getString("GetData", "00/00");
            TextViews.setText("GetClick: " + GetClick + " GetDatas: " + GetData);
        }
    }
    //CLICKNOFRAUD: METODOS DE VEZESPROCESSADAS, CLICKFRAUD, SALVADATAS



    public void loadAd() {
        //Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
        AdRequest adRequest = new AdRequest.Builder().build();
        String AD_UNIT_ID = getString(R.string.app_inst_id);
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {

                                    public void onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "Ad was clicked.");
                                        ClickNoFraud();


                                    }
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        loadAd();

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                        loadAd();

                                    }
                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                        //showInterstitial();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;


                        @SuppressLint("DefaultLocale")
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        //Toast.makeText(MainActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();

        }
    }

}
