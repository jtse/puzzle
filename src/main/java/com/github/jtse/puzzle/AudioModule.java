package com.github.jtse.puzzle;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

class AudioModule extends AbstractModule {

  private static final class NoopAudio implements Audio {

    @Override
    public int getBufferID() {
      return 0;
    }

    @Override
    public float getPosition() {
      return 0;
    }

    @Override
    public boolean isPlaying() {
      return false;
    }

    @Override
    public int playAsMusic(float arg0, float arg1, boolean arg2) {
      return 0;
    }

    @Override
    public int playAsSoundEffect(float arg0, float arg1, boolean arg2) {
      return 0;
    }

    @Override
    public int playAsSoundEffect(float arg0, float arg1, boolean arg2, float arg3, float arg4, float arg5) {
      return 0;
    }

    @Override
    public boolean setPosition(float arg0) {
      return false;
    }

    @Override
    public void stop() {
    }
  }
  
  @Override
  protected void configure() {
  }

  @Provides
  Supplier<Audio> providesAudioSupplier(
      @Named("@base-path") File basePath, @Named("collision-sound") String sound) {

    if (sound.isEmpty()) {
      return Suppliers.<Audio>ofInstance(new NoopAudio());
    }

    final File soundFile = new File(basePath, sound);
    checkState(soundFile.exists(), "soundFile not found");
    checkState(soundFile.isFile(), "soundFile not a file");
    checkState(soundFile.canRead(), "soundFile cannot be read");
    
    return new Supplier<Audio>() {
      @Override
      public Audio get() {
        try (InputStream in = ResourceLoader.getResourceAsStream(soundFile.getAbsolutePath())) {
          return AudioLoader.getAudio("WAV", in);
        } catch (IOException e) {
          throw new RuntimeException("Could supply collision sound file", e);
        }
      }
    };
  }
}
