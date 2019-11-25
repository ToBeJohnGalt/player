package com.aliyun.vodplayerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.source.UrlSource;

public class AliyunVodPlayerView extends RelativeLayout {
    private SurfaceView surfaceView;
    private AliPlayer aliPlayer;
    private int state = IPlayer.idle;

    public AliyunVodPlayerView(Context context) {
        super(context);
        initVideoView();
    }

    public AliyunVodPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public AliyunVodPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }

    private void initVideoView() {
        initSurfaceView();
        initAliVcPlayer();
    }

    private void reset() {
        stop();
    }

    private void initSurfaceView() {
        surfaceView = new SurfaceView(getContext().getApplicationContext());
        addSubView(surfaceView);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (aliPlayer != null) {
                    aliPlayer.setDisplay(surfaceHolder);
                    aliPlayer.redraw();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width,
                                       int height) {
                if (aliPlayer != null) {
                    aliPlayer.redraw();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (aliPlayer != null) {
                    aliPlayer.setDisplay(null);
                }
            }
        });
    }

    private void initAliVcPlayer() {
        aliPlayer = AliPlayerFactory.createAliPlayer(getContext().getApplicationContext());
        aliPlayer.setDisplay(surfaceView.getHolder());
    }

    public void setLocalSource(UrlSource aliyunLocalSource) {
        if (aliPlayer == null) {
            return;
        }

        clearAllSource();
        reset();

        prepareLocalSource(aliyunLocalSource);

    }

    private void addSubView(View view) {
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    private void clearAllSource() {
    }

    private void prepareLocalSource(UrlSource aliyunLocalSource) {
        aliPlayer.setAutoPlay(true);
        aliPlayer.setDataSource(aliyunLocalSource);
        aliPlayer.prepare();
    }

    public void onResume() {
        resumePlayerState();
    }

    public void onStop() {
        savePlayerState();
    }

    private void resumePlayerState() {
        if (aliPlayer == null) {
            return;
        }
        start();
    }

    private void savePlayerState() {
        if (aliPlayer == null) {
            return;
        }
        pause();
    }

    public void onDestroy() {
        stop();
        if (aliPlayer != null) {
            aliPlayer.release();
            aliPlayer = null;
        }

        surfaceView = null;
    }

    public void start() {

        if (aliPlayer == null) {
            return;
        }

        if (state == IPlayer.paused || state == IPlayer.prepared) {
            aliPlayer.start();
        }

    }

    public void pause() {
        if (aliPlayer == null) {
            return;
        }

        if (state == IPlayer.started || state == IPlayer.prepared) {
            aliPlayer.pause();
        }
    }

    private void stop() {
        if (aliPlayer != null) {
            aliPlayer.stop();
        }
    }

    public void setAutoPlay(boolean auto) {
        if (aliPlayer != null) {
            aliPlayer.setAutoPlay(auto);
        }
    }

    public PlayerConfig getPlayerConfig() {
        if (aliPlayer != null) {
            return aliPlayer.getConfig();
        }
        return null;
    }

    public void setPlayerConfig(PlayerConfig playerConfig) {
        if (aliPlayer != null) {
            aliPlayer.setConfig(playerConfig);
        }
    }

}
