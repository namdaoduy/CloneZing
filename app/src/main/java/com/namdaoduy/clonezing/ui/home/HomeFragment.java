package com.namdaoduy.clonezing.ui.home;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.namdaoduy.clonezing.MainActivity;
import com.namdaoduy.clonezing.R;
import com.namdaoduy.clonezing.Song;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View view;
    private ImageView playButton, nextButton, prevButton;
    private TextView textTitle, textArtist;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private int position = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        findIDs();
        addSongs();
        setListeners();

        return view;
    }

    private void findIDs() {
        playButton = view.findViewById(R.id.playButton);
        nextButton = view.findViewById(R.id.nextButton);
        prevButton = view.findViewById(R.id.prevButton);
        textTitle = view.findViewById(R.id.textTitle);
        textArtist = view.findViewById(R.id.textArtist);
        seekBar = view.findViewById(R.id.seekBar);
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
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    resumeSong();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = position + 1;
                if (position > songs.size() - 1) {
                    position = 0;
                }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                createMedia();
                startSong();
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
                }
                createMedia();
                startSong();
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
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        textTitle.setText(songs.get(position).getTitle());
        textArtist.setText(songs.get(position).getArtist());
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
    }

    private void resumeSong() {
        int length = mediaPlayer.getCurrentPosition();
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        textTitle.setText(songs.get(position).getTitle());
        textArtist.setText(songs.get(position).getArtist());
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(length);
    }

    private void createMedia() {
        MainActivity context = MainActivity.getInstanceActivity();
        mediaPlayer = MediaPlayer.create(context, songs.get(position).getFile());
    }
}
