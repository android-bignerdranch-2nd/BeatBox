package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAsset;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAsset = context.getAssets();
        // 이 생성자는 deprecated 되었지만 호환성 유지를 위해 필요하다.
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }

    private void loadSounds() {
        String[] soundNames;

        try {
            soundNames = mAsset.list(SOUNDS_FOLDER);
            Log.i(TAG, "FOUND " + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.e(TAG, "Could not list assets", e);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assertPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assertPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException e) {
                Log.e(TAG, "Could not load sount " + filename, e);
            }
        }

    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAsset.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
