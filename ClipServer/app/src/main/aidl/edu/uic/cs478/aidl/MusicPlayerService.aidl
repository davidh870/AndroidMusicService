// MusicPlayerService.aidl
package edu.uic.cs478.aidl;

// Declare any non-default types here with import statements

interface MusicPlayerService {
    void startSong(in String songID);
    void playSong();
    void pauseSong();
    void stopSong();
}
