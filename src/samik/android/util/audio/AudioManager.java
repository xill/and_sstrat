package samik.android.util.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * AudioManager -class is designed for longer sound media.
 * Loaded files should always be free'd after use to conserve memory.
 */
public class AudioManager {
	private MediaPlayer mp;
	private static boolean muted = false;
	private static boolean hasChanged = false; 
	private int m_trackId;
	public float streamVolume;
	public boolean isLoaded = false;
	
	public AudioManager(){}
	
	private final float MUSIC_VOLUME = 0.65f;
	
	public AudioManager(Context context, int trackId){
		try {
			load(context,trackId);
		} catch(Exception e){
			isLoaded = false;
			Log.e("AudioManager","AudioManager loading caused an error.");
		}
	}
	
	protected void finalize(){
		free();
	}
	
	/**
	 * Load a new media file.
	 * 
	 * @param context
	 * @param trackId
	 */
	public void load(Context context, int trackId){
		if(this.mp != null){
			this.mp.reset();
		}
		this.m_trackId = trackId;
		this.mp = MediaPlayer.create(context, trackId);
		android.media.AudioManager audioManager = (android.media.AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		//StreamVolume is used to get the current volume setting
		streamVolume = audioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
		//divided by MaxVolume to get float between 0.0 and 1.0
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC);

		if(streamVolume <= 0){
			/* to ensure that volume wont stay muted if the volume levels are raised. */
			streamVolume = 0.1f;
		}
		
		if(muted){
			this.mp.setVolume(0, 0);
		}
		else {
			this.mp.setVolume(MUSIC_VOLUME, MUSIC_VOLUME);
		}
		isLoaded = true;
	}
	
	/**
	 * Free all memory used by the current media file.
	 */
	public void free(){
		if(this.mp != null){
			try {
				this.mp.release();
			} catch(Exception e) {
				Log.e("AudioManager","AudioManager memory release caused an error.");
			}
		}
	}
	
	/**
	 * Start the current media file.
	 * 
	 * If the file was stopped this will restart the playback.
	 */
	public void start(){
		if(this.mp != null){
			try {
				this.mp.start();
			} catch (Exception e){
				Log.e("AudioManager","AudioManager startup caused an error.");
			}
		}
	}
	
	/**
	 * Pause the current media file.
	 * 
	 * Calling start will restart the media from the same spot it was paused on.
	 */
	public void pause(){
		if(this.mp != null){
			try {
			this.mp.pause();
			} catch (Exception e){
				Log.e("AudioManager","AudioManager pause caused an error.");
			}
		}
	}
	
	/**
	 * Stops the current media file.
	 * 
	 * Calling start will restart the media from the beginning.
	 */
	public void stop(){
		if(this.mp != null){
			try {
				this.mp.stop();
			} catch (Exception e){
				Log.e("AudioManager","AudioManager stop caused an error.");
			}
		}
	}
	
	/**
	 * Change the players loop status.
	 * 
	 * @param looping
	 */
	public void setLooping(boolean looping){
		if(this.mp != null){
			try {
				this.mp.setLooping(looping);
			} catch (Exception e) {
				Log.e("AudioManager","AudioManager setLooping caused an error.");
			}
		}
	}
	
	/**
	 * Is the player looping or not?
	 * 
	 * @return
	 */
	public boolean isLooping(){
		return (this.mp != null)
				?this.mp.isLooping()
				:false;
	}
	
	/**
	 * Mute or unmute the player.
	 * 
	 * @param mute
	 */
	public static void setMuted(boolean mute){
		muted = mute;
		hasChanged = true;
	}
	
	/**
	 * Is the player muted?
	 * 
	 * @return
	 */
	public static boolean isMuted(){
		return muted;
	}
	
	/**
	 * has the audiomanagers state changed?
	 * like if it was muted or unmuted.
	 * 
	 * changed status is reset after first call of this function.
	 * 
	 * @return
	 */
	public static boolean hasChanged(){
		if(hasChanged){
			hasChanged = false;
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the current position of the media file.
	 * 
	 * @return current position in milliseconds.
	 */
	public int getCurrentPosition(){
		return (this.mp != null)
				?this.mp.getCurrentPosition()
				:0;
	}
	
	/**
	 * Seeks the specific time position.
	 */
	public void seekTo(int msec){
		if(this.mp != null)
			this.mp.seekTo(msec);
	}
	
	/**
	 * True is currently playing. False otherwise.
	 * 
	 * @return
	 */
	public boolean isPlaying(){
		return (this.mp != null)
				?this.mp.isPlaying()
				:false;
	}
	
	public int getDuration(){
		if(this.mp != null){
			return this.mp.getDuration();
		}
		else return 0;
	}
	
	public boolean holdsThisFile(int id){
		return (m_trackId == id)? true :false;
	}
	
	/**
	 * Update the status of this AudioManager.
	 */
	public void update(){
		if(this.mp != null){
			try {
				if(muted){
					this.mp.setVolume(0,0);
				}
				else {
					this.mp.setVolume(MUSIC_VOLUME, MUSIC_VOLUME);
				}
			} catch (Exception e){
				Log.e("AudioManager","AudioManager update caused an error.");
			}
		}
	}
}
