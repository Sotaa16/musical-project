package com.example.mucialproject

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class DrumSounds(context: Context) {

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(8)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val soundIds = HashMap<DrumPiece, Int>()

    init {
        soundIds[DrumPiece.CRASH_L] = pool.load(context, R.raw.crash, 1)
        soundIds[DrumPiece.SPLASH] = pool.load(context, R.raw.splash, 1)
        soundIds[DrumPiece.TOM_1] = pool.load(context, R.raw.tom1, 1)
        soundIds[DrumPiece.TOM_2] = pool.load(context, R.raw.tom2, 1)
        soundIds[DrumPiece.TOM_3] = pool.load(context, R.raw.tom3, 1)
        soundIds[DrumPiece.CRASH_R] = pool.load(context, R.raw.crash2, 1)
        soundIds[DrumPiece.RIDE] = pool.load(context, R.raw.ride, 1)
        soundIds[DrumPiece.RIDE_BELL] = pool.load(context, R.raw.ridebell, 1)
        soundIds[DrumPiece.HH_CLOSE] = pool.load(context, R.raw.hh_close, 1)
        soundIds[DrumPiece.HH_OPEN] = pool.load(context, R.raw.hh_open, 1)
        soundIds[DrumPiece.SNARE] = pool.load(context, R.raw.snare, 1)
        soundIds[DrumPiece.KICK_L] = pool.load(context, R.raw.kick, 1)
        soundIds[DrumPiece.KICK_R] = pool.load(context, R.raw.kick, 1)
        soundIds[DrumPiece.FLOOR_TOM] = pool.load(context, R.raw.floor_tom, 1)
    }

    fun play(piece: DrumPiece) {
        soundIds[piece]?.let { pool.play(it, 1f, 1f, 1, 0, 1f) }
    }

    fun release() {
        pool.release()
    }
}