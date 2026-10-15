# Practical 13: Graphics and Custom Views Using Canvas

## 1. Aim
13. Graphics and custom views + canvas

## 2. Theory
The Android framework provides a powerful hardware-accelerated 2D graphics rendering engine centered around the **Canvas** and **Paint** classes.

### Core Concepts:
1. **Custom View**: Creating a subclass of `android.view.View` and overriding the `onDraw(Canvas canvas)` method allows developers to create bespoke visual widgets, charts, and diagrams not possible with standard platform widgets.
2. **The Canvas (`android.graphics.Canvas`)**: The drawing surface that records drawing commands. It provides geometric primitive methods such as:
   - `canvas.drawRect()` / `canvas.drawRoundRect()`: Draws rectangles with optional rounded corner radii.
   - `canvas.drawCircle()`: Draws circles specified by center coordinates \((x, y)\) and radius \(r\).
   - `canvas.drawLine()`: Draws line segments between points \((x_1, y_1)\) and \((x_2, y_2)\).
   - `canvas.drawArc()`: Draws elliptical arc sections or filled pie wedges.
   - `canvas.drawText()`: Renders styled typographic strings.
3. **The Paint (`android.graphics.Paint`)**: Holds the style, color, stroke width, text size, and shader definitions used to draw each geometry:
   - `setStyle(Paint.Style.FILL / STROKE / FILL_AND_STROKE)`
   - `setColor(int color)`
   - `setStrokeWidth(float width)`
   - `setAntiAlias(boolean flag)`: Smooths jagged pixel edges.
4. **Android Rendering Pipeline & Invalidation**:
   - `onDraw()` must remain extremely efficient; memory allocations (such as `new Paint()`) must never occur in `onDraw()` to avoid garbage collection stutter.
   - When data or state changes, calling `invalidate()` marks the view as dirty and schedules a layout/redraw pass on the UI thread.
   - For background threads, `postInvalidate()` is called.
5. **Interactive Touch Handling**: Overriding `onTouchEvent(MotionEvent event)` allows the custom view to track touch down, move, and up actions, recalculating coordinates and redrawing dynamically.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Create Custom View Class**: Create `CustomCanvasView` extending `View`.
2. **Implement Constructors**: Provide constructors accepting `(Context)` and `(Context, AttributeSet)` for XML layout inflation.
3. **Initialize Paint Objects**: In `init()`, configure distinct `Paint` objects for rectangle, circle, line, arc, text, and touch markers. Enable anti-aliasing.
4. **Override `onDraw(Canvas canvas)`**:
   - Measure view dimensions via `getWidth()` and `getHeight()`.
   - Render title text using `canvas.drawText()`.
   - Draw rounded rectangle with `canvas.drawRoundRect()`.
   - Draw concentric circles (filled inner circle + stroked outer circle).
   - Draw separator line with `canvas.drawLine()`.
   - Draw 270-degree arc slice with `canvas.drawArc()`.
   - If user is touching, draw glowing touch marker and coordinate labels at `(touchX, touchY)`.
5. **Handle Touch Events**: Override `onTouchEvent()`. For `ACTION_DOWN` and `ACTION_MOVE`, store event coordinates and call `invalidate()`.
6. **Host in Activity**: Place `<com.example.practical13.CustomCanvasView>` in `activity_main.xml`. Connect control buttons in `MainActivity` to toggle color styles and reset the canvas.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical13/MainActivity.java)
- [CustomCanvasView.java](file:///app/src/main/java/com/example/practical13/CustomCanvasView.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [button_bg.xml](file:///app/src/main/res/drawable/button_bg.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_13_Custom_Graphics_Canvas` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or device.
4. Observe the rendered shapes: rounded rectangle, circle, arc, and line.
5. Tap or drag your finger anywhere on the canvas to see the touch tracking circle follow your touch in real time.
6. Tap **Toggle Drawing Style** to change color palettes.
7. Tap **Reset View** to reset the touch marker.

## 11. Expected Output
- The custom canvas renders sharp 2D graphics with proper anti-aliasing.
- Geometric shapes are clearly labeled.
- Touching the screen immediately updates the canvas with a marker showing exact \((x, y)\) coordinates.
- Tapping **Toggle Drawing Style** smoothly refreshes the canvas with teal, purple, and orange color themes.

## 12. Testing
| Test Case ID | Test Action | Expected Result | Status |
|---|---|---|---|
| TC_13_01 | Canvas Initial Render | All shapes (roundRect, circle, line, arc, text) draw correctly | Pass |
| TC_13_02 | Touch Down Event | Marker circle and coordinates appear at finger position | Pass |
| TC_13_03 | Drag Touch Movement | Marker smoothly follows touch movement without frame lag | Pass |
| TC_13_04 | Toggle Style Button | Color palette changes and canvas invalidates immediately | Pass |
| TC_13_05 | Reset Button | Touch marker cleared; returns to initial state | Pass |

## 13. Common Errors and Solutions
- **Error**: View does not appear in XML preview.
  - **Solution**: Ensure you implemented the two-parameter constructor `CustomCanvasView(Context context, AttributeSet attrs)`.
- **Error**: App stutters or drops frames during drawing.
  - **Solution**: Never allocate new objects (like `new Paint()` or `new RectF()`) inside `onDraw()`. Initialize them in constructor or `init()`.
- **Error**: `invalidate()` called from worker thread crashes app.
  - **Solution**: Use `postInvalidate()` when requesting a redraw from a non-UI thread.

## 14. Viva Questions and Answers
1. **Q: What is the difference between `Canvas` and `Paint` in Android?**
   - **A**: `Canvas` represents the drawing surface and defines *what* geometry to draw (circles, rectangles, lines). `Paint` defines *how* to draw it (color, stroke width, style, anti-aliasing, shaders).
2. **Q: Why must object allocation be avoided inside `onDraw()`?**
   - **A**: `onDraw()` can be called 60 to 120 times per second during animations or touches. Allocating objects inside `onDraw()` triggers frequent Garbage Collection (GC) pauses, causing frame drops and visual stutter (jank).
3. **Q: What does `invalidate()` do?**
   - **A**: `invalidate()` signals to the Android view hierarchy that the view's content is outdated and must be redrawn, causing the system to schedule an `onDraw()` call during the next frame pass.
4. **Q: What is the difference between `invalidate()` and `postInvalidate()`?**
   - **A**: `invalidate()` can only be called from the Main (UI) thread. `postInvalidate()` can be called safely from any background worker thread; it posts a message to the UI thread to trigger the redraw.
5. **Q: How does `useCenter` in `drawArc()` affect the rendered shape?**
   - **A**: If `useCenter` is `true`, the arc includes the center point, creating a closed pie slice/wedge. If `false`, only the curved boundary arc is drawn (or filled as a chord segment).
