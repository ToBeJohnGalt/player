package com.aliyun.vodplayerview.widget;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.source.UrlSource;
import com.aliyun.vodplayer.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class AliyunPlayerSkinActivity extends AppCompatActivity {

    private AliyunVodPlayerView aliyunVodPlayerView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_player_layout_skin);
        initView();
        initAliyunPlayerView();
    }

    private void initView() {
        Button btnViewLive = findViewById(R.id.btn_view_default_live);
        Button btnScanToViewLive = findViewById(R.id.btn_scan_to_view_live);
        Button btnInput = findViewById(R.id.btn_input);
        btnViewLive.setOnClickListener(view -> viewLive());
        btnScanToViewLive.setOnClickListener(view -> scanToViewLive());
        btnInput.setOnClickListener(view -> inputLiveUrl());
    }

    private void inputLiveUrl() {
        new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.input_live_link)
            .setView(R.layout.edit_text)
            .setPositiveButton(
                R.string.confirm,
                (dialog, which) -> onLiveUrlConfirmed((AlertDialog) dialog)).show();
    }

    private void onLiveUrlConfirmed(AlertDialog dialog) {
        EditText input = dialog.findViewById(R.id.et_link);
        play(Objects.requireNonNull(input).getText().toString());
    }

    private void scanToViewLive() {
        Toast.makeText(this, "todo", Toast.LENGTH_SHORT).show();
    }


    private void viewLive() {
        play(PlayUrlConstant.PLAY_PARAM_URL);
    }

    private void initAliyunPlayerView() {
        aliyunVodPlayerView = findViewById(R.id.video_view);
        aliyunVodPlayerView.setKeepScreenOn(true);
        aliyunVodPlayerView.setAutoPlay(true);

    }

    private void play(String url) {
        Toast.makeText(this, "开始加入直播:" + url, Toast.LENGTH_SHORT).show();
        UrlSource urlSource = new UrlSource();
        urlSource.setUri(url);
        int maxDelayTime = 100;
        PlayerConfig playerConfig = aliyunVodPlayerView.getPlayerConfig();
        playerConfig.mMaxDelayTime = maxDelayTime;
        aliyunVodPlayerView.setPlayerConfig(playerConfig);
        aliyunVodPlayerView.setLocalSource(urlSource);
    }


    @Override
    protected void onResume() {
        super.onResume();
        aliyunVodPlayerView.setAutoPlay(true);
        aliyunVodPlayerView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        aliyunVodPlayerView.setAutoPlay(false);
        aliyunVodPlayerView.onStop();
    }


    @Override
    protected void onDestroy() {
        aliyunVodPlayerView.onDestroy();
        aliyunVodPlayerView = null;
        super.onDestroy();
    }

}
