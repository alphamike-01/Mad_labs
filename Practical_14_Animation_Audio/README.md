# Practical 14: Animation and Multimedia (Audio Playback)

## 1. Aim
14. Animation + Multimedia (Audio Playback)

## 2. Theory
Mobile applications often integrate multimedia playback and fluid property animations to deliver engaging and intuitive user experiences.

### Multimedia Architecture (Android MediaPlayer):
The `android.media.MediaPlayer` class controls the playback of audio and video files and streams.
- **State Machine**: `MediaPlayer` operates on an internal state machine (Idle -> Initialized -> Prepared -> Started -> Paused -> Stopped -> Released). Calling methods out of state order throws an `IllegalStateException`.
- **Bundled Raw Resources**: Bundling media inside `res/raw/` (e.g. `res/raw/sample_chime.wav`) allows playback via `MediaPlayer.create(context, R.raw.resource_id)` which automatically handles preparation.
- **Resource Management**: Audio decoders hold unmanaged native hardware resources. Failing to call `mediaPlayer.release()` inside `onDestroy()` causes native memory and audio hardware channel leaks.

### Animation Framework (Property Animation):
Introduced in Android 3.0, the Property Animation framework (`android.animation`) alters any object property over time:
- **`ObjectAnimator`**: Animates a specific property (e.g. `"rotation"`, `"scaleX"`, `"alpha"`) of a target view by calling the property's setter method via reflection.
- **`LinearInterpolator`**: Provides a constant rate of change for steady rotational velocity.
- **`ViewPropertyAnimator`**: Provides a fluent API (`view.animate().scaleX().setDuration()`) for quick multi-property animations with high performance.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23
- **Bundled Audio Asset**: 16-bit 22050Hz PCM WAV file (`res/raw/sample_chime.wav`), royalty-free and generated specifically for this project.

## 4. Algorithm
1. **Bundle Audio Resource**: Place `sample_chime.wav` in `app/src/main/res/raw/`.
2. **Setup ObjectAnimator**:
   - Instantiate `ObjectAnimator.ofFloat(ivVinyl, "rotation", 0f, 360f)`.
   - Set duration to 3000ms, repeat count to `INFINITE`, and interpolator to `LinearInterpolator`.
3. **Play Control**:
   - On Play button tap: If `mediaPlayer == null`, call `MediaPlayer.create(this, R.raw.sample_chime)`.
   - Register `setOnCompletionListener` to reset UI when audio ends.
   - Call `mediaPlayer.start()`.
   - Start or resume `vinylRotationAnimator`.
   - Start periodic progress tracking handler.
4. **Pause Control**:
   - On Pause button tap: Check `mediaPlayer.isPlaying()`. Call `mediaPlayer.pause()`.
   - Pause rotation with `vinylRotationAnimator.pause()`.
5. **Stop Control**:
   - On Stop button tap: Stop `mediaPlayer`, release it, and set reference to `null`.
   - Cancel animation and reset `ivVinyl.setRotation(0f)`.
6. **Activity Teardown**: In `onDestroy()`, ensure `mediaPlayer.release()` and `vinylRotationAnimator.cancel()` are called.

## 5. Complete Source Code
See [MainActivity.java](file:///app/src/main/java/com/example/practical14/MainActivity.java).

## 6. XML Layouts
See [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml).

## 7. AndroidManifest.xml
See [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml).

## 8. Resources
- **Audio Asset**: [sample_chime.wav](file:///app/src/main/res/raw/sample_chime.wav)
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [ic_vinyl_record.xml](file:///app/src/main/res/drawable/ic_vinyl_record.xml), [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [button_bg.xml](file:///app/src/main/res/drawable/button_bg.xml)

## 9. Build Configuration
See [build.gradle](file:///app/build.gradle).

## 10. Execution Instructions
1. Open `Practical_14_Animation_Audio` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or device with audio enabled.
4. Tap **Play**: Listen to the chime and watch the vinyl record rotate continuously.
5. Tap **Pause**: Audio pauses and the vinyl freezes at its current angle.
6. Tap **Play**: Audio and rotation resume seamlessly.
7. Tap **Stop / Reset**: Audio stops, player releases, and vinyl returns to angle 0.

## 11. Expected Output
- High-fidelity chime audio plays through device speaker.
- Vinyl record spins smoothly in synchronization with audio playback.
- Status text updates ("Status: Playing Audio", "Status: Paused", "Status: Stopped").
- Tapping buttons triggers a tactile scale-pulse animation.

## 12. Testing
| Test Case ID | Action | Expected Result | Status |
|---|---|---|---|
| TC_14_01 | Tap Play from Idle | Audio plays; vinyl record begins 360-degree rotation | Pass |
| TC_14_02 | Tap Pause while Playing | Audio pauses; vinyl animation freezes at current angle | Pass |
| TC_14_03 | Tap Play after Pause | Audio resumes from paused position; vinyl continues spinning | Pass |
| TC_14_04 | Tap Stop | Audio stops; player is released; vinyl rotation resets to 0 degrees | Pass |
| TC_14_05 | Natural Track End | `onCompletion()` fires; vinyl stops spinning and resets | Pass |

## 13. Common Errors and Solutions
- **Error**: `IllegalStateException` thrown by `MediaPlayer`.
  - **Solution**: Always follow the MediaPlayer lifecycle state machine. Do not call `start()` or `pause()` when player is in `Stopped` or `Released` state.
- **Error**: Audio continues playing after closing the app.
  - **Solution**: Always invoke `mediaPlayer.stop()` and `mediaPlayer.release()` in `onDestroy()`.
- **Error**: Animation is jerky or freezes.
  - **Solution**: Use `ObjectAnimator` with hardware acceleration enabled (default in modern Android) and set a `LinearInterpolator`.

## 14. Viva Questions and Answers
1. **Q: What is the lifecycle state machine of MediaPlayer?**
   - **A**: `MediaPlayer` transitions through states: Idle -> Initialized -> Prepared -> Started -> Paused -> Stopped -> PlaybackCompleted -> End (Released). Calling methods in incompatible states results in an `IllegalStateException`.
2. **Q: Why must `mediaPlayer.release()` be called in `onDestroy()`?**
   - **A**: MediaPlayer manages native audio hardware codecs and memory outside the Java Garbage Collector. Calling `release()` frees these native system resources immediately.
3. **Q: What is the difference between View Animation (Tween) and Property Animation?**
   - **A**: View Animation (Tween) only altered where the view was drawn on screen without modifying the actual `View` object properties (e.g. click hit-boxes remained at original coordinates). Property Animation actually mutates the target object's real properties over time.
4. **Q: How does `ObjectAnimator` work?**
   - **A**: `ObjectAnimator` takes a target object, a property name (e.g. `"rotation"`), and property values. It repeatedly invokes the setter method (e.g. `setRotation()`) via reflection at each frame interval calculated by the interpolator.
5. **Q: Where should static audio and media files be stored in an Android project?**
   - **A**: In the `res/raw/` directory. Android preserves files in `res/raw/` without compression, assigning each an integer ID in `R.raw.*` accessible directly via `R.raw.file_name`.
