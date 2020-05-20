package com.namdaoduy.clonezing.ui.home;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.namdaoduy.clonezing.MainActivity;
import com.namdaoduy.clonezing.R;
import com.namdaoduy.clonezing.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View view;
    private ImageView playButton, nextButton, prevButton, circleImage;
    private TextView textTitle, textArtist, textTimeCurrent, textTimeTotal;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private int position = 0;
    private final Handler timer = new Handler();
    private Runnable updatePerSecond = new Runnable() {
        @Override
        public void run() {
            int length = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(length);
            textTimeCurrent.setText(intToTimeString(length));
            timer.postDelayed(this, 1000);
        }
    };
    private ObjectAnimator rotateAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.getInstanceActivity(), R.animator.rotate);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        findIDs();
        addSongs();
        setListeners();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            rotateAnimator.cancel();
        }
    }

    private String intToTimeString(int ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(ms);
    }

    private void findIDs() {
        playButton = view.findViewById(R.id.playButton);
        nextButton = view.findViewById(R.id.nextButton);
        prevButton = view.findViewById(R.id.prevButton);
        textTitle = view.findViewById(R.id.textTitle);
        textArtist = view.findViewById(R.id.textArtist);
        seekBar = view.findViewById(R.id.seekBar);
        textTimeCurrent = view.findViewById(R.id.textTimeCurrent);
        textTimeTotal = view.findViewById(R.id.textTimeTotal);
        circleImage = view.findViewById(R.id.circleImage);
    }

    private void nextHandler() {
        position = position + 1;
        if (position > songs.size() - 1) {
            position = 0;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            rotateAnimator.cancel();
            timer.removeCallbacks(updatePerSecond);
        }
        createMedia();
        startSong();
    }

    private void setListeners() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer == null) {
                    if (position >= 0 && position <= songs.size() - 1) {
                        createMedia();
                        startSong();
                    }
                } else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    rotateAnimator.pause();
                    timer.removeCallbacks(updatePerSecond);
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    resumeSong();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextHandler();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = position - 1;
                if (position < 0) {
                    position = songs.size() - 1;
                }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    rotateAnimator.cancel();
                    timer.removeCallbacks(updatePerSecond);
                }
                createMedia();
                startSong();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        rotateAnimator.pause();
                        resumeSong();
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void addSongs() {
        songs = new ArrayList<Song>();
        songs.add(new Song("Chạm Đáy Nỗi Đau", "ERIK", R.raw.chamdaynoidau));
        songs.add(new Song("Em Là Tất Cả", "Đức Phúc", R.raw.emlatatca));
        songs.add(new Song("Bài Ca Tuổi Trẻ", "Da LAB", R.raw.baicatuoitre));
    }

    private void startSong() {
        mediaPlayer.start();
        rotateAnimator.start();
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        textTitle.setText(songs.get(position).getTitle());
        textArtist.setText(songs.get(position).getArtist());
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        textTimeTotal.setText(intToTimeString(mediaPlayer.getDuration()));
        textTimeCurrent.setText("00:00");
        timer.postDelayed(updatePerSecond, 1000);
    }

    private void resumeSong() {
        int length = mediaPlayer.getCurrentPosition();
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
        rotateAnimator.resume();
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        textTitle.setText(songs.get(position).getTitle());
        textArtist.setText(songs.get(position).getArtist());
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(length);
        timer.postDelayed(updatePerSecond, 1000);
    }

    private void createMedia() {
        MainActivity context = MainActivity.getInstanceActivity();
        mediaPlayer = MediaPlayer.create(context, songs.get(position).getFile());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextHandler();
            }
        });
        rotateAnimator.setTarget(circleImage);
    }
}
