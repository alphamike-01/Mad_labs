package com.example.practical14;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Practical 14: Animation and Multimedia (Audio Playback)
 * Demonstrates audio playback via Android MediaPlayer (Play, Pause, Stop)
 * coupled with smooth continuous ObjectAnimator rotation on an album vinyl graphic.
 */
public class MainActivity extends AppCompatActivity {

    private ImageView ivVinyl;
    private TextView tvAudioStatus, tvAudioTime;
    private Button btnPlay, btnPause, btnStop;

    private MediaPlayer mediaPlayer;
    private ObjectAnimator vinylRotationAnimator;
    private Handler progressHandler;
    private Runnable progressRunnable;

    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivVinyl = findViewById(R.id.ivVinyl);
        tvAudioStatus = findViewById(R.id.tvAudioStatus);
        tvAudioTime = findViewById(R.id.tvAudioTime);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);

        progressHandler = new Handler(Looper.getMainLooper());

        // Setup ObjectAnimator for smooth continuous rotation
        setupVinylAnimation();

        // Setup Button Listeners
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulseView(v);
                playAudio();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulseView(v);
                pauseAudio();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulseView(v);
                stopAudio();
            }
        });
    }

    /**
     * Initializes ObjectAnimator to rotate the vinyl record 360 degrees indefinitely.
     */
    private void setupVinylAnimation() {
        vinylRotationAnimator = ObjectAnimator.ofFloat(ivVinyl, "rotation", 0f, 360f);
        vinylRotationAnimator.setDuration(3000); // 3 seconds per full rotation
        vinylRotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        vinylRotationAnimator.setInterpolator(new LinearInterpolator());
    }

    /**
     * Subtle pulse animation on control buttons when tapped.
     */
    private void pulseView(View view) {
        view.animate().scaleX(0.92f).scaleY(0.92f).setDuration(80).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start();
            }
        }).start();
    }

    /**
     * Starts or resumes audio playback and vinyl animation.
     */
    private void playAudio() {
        if (mediaPlayer == null) {
            // Initialize MediaPlayer with bundled raw audio
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_chime);
            if (mediaPlayer == null) {
                Toast.makeText(this, "Failed to load audio resource!", Toast.LENGTH_SHORT).show();
                return;
            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onPlaybackCompleted();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(MainActivity.this, "MediaPlayer Error: " + what, Toast.LENGTH_SHORT).show();
                    stopAudio();
                    return true;
                }
            });
        }

        mediaPlayer.start();
        isPaused = false;

        // Resume or start rotation animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && vinylRotationAnimator.isPaused()) {
            vinylRotationAnimator.resume();
        } else if (!vinylRotationAnimator.isStarted()) {
            vinylRotationAnimator.start();
        }

        tvAudioStatus.setText("Status: Playing Audio");
        tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.green_700));
        startTrackingProgress();

        Toast.makeText(this, "Playback Started", Toast.LENGTH_SHORT).show();
    }

    /**
     * Pauses playback and freezes rotation at current angle.
     */
    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                vinylRotationAnimator.pause();
            }

            tvAudioStatus.setText("Status: Paused");
            tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.amber_700));
            stopTrackingProgress();

            Toast.makeText(this, "Playback Paused", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Stops playback, releases player, and resets vinyl rotation.
     */
    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        isPaused = false;
        vinylRotationAnimator.cancel();
        ivVinyl.setRotation(0f);

        tvAudioStatus.setText("Status: Stopped / Reset");
        tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.red_700));
        tvAudioTime.setText("Position: 0:00 / 0:02");
        stopTrackingProgress();

        Toast.makeText(this, "Playback Stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when audio finishes playing naturally.
     */
    private void onPlaybackCompleted() {
        vinylRotationAnimator.cancel();
        ivVinyl.setRotation(0f);
        tvAudioStatus.setText("Status: Playback Completed");
        tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.purple_700));
        stopTrackingProgress();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Toast.makeText(this, "Track Finished", Toast.LENGTH_SHORT).show();
    }

    private void startTrackingProgress() {
        stopTrackingProgress();
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentSec = mediaPlayer.getCurrentPosition() / 1000;
                    int totalSec = mediaPlayer.getDuration() / 1000;
                    tvAudioTime.setText(String.format("Position: 0:%02d / 0:%02d", currentSec, totalSec));
                    progressHandler.postDelayed(this, 300);
                }
            }
        };
        progressHandler.post(progressRunnable);
    }

    private void stopTrackingProgress() {
        if (progressRunnable != null) {
            progressHandler.removeCallbacks(progressRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTrackingProgress();
        // Crucial resource cleanup to prevent native audio hardware leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (vinylRotationAnimator != null) {
            vinylRotationAnimator.cancel();
        }
    }
}
