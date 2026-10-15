# Practical 11: Dynamic Views Generation

## 1. Aim
11. Develop an application that accepts a number from user and then dynamically generates accepted number of items in another activity.

## 2. Theory
In native Android development, user interface components can be created either declaratively in XML or programmatically at runtime using Java. Dynamic view instantiation is critical for applications that render variable numbers of items (such as shopping carts, survey questions, or feed cards) determined dynamically at runtime.

### Key Concepts:
1. **Programmatic View Creation**: Instantiating Android View subclasses directly in Java (e.g. `new LinearLayout(context)`, `new TextView(context)`, `new Button(context)`).
2. **`ViewGroup.LayoutParams`**: Every dynamically instantiated view must be assigned layout parameters specifying its width, height, margins, and weight relative to its parent container.
3. **ScrollView Container**: When dynamically generating content exceeding screen height, wrapping the view container in a `ScrollView` enables smooth vertical scrolling.
4. **Density-Independent Conversions**: Hardcoded pixel values do not scale across devices. Converting dp values to pixels via `Math.round(dp * density)` ensures consistent spacing across screen densities.
5. **Safety Bounds**: Unchecked dynamic view generation can exhaust device memory (OutOfMemoryError) or freeze the main thread. Implementing input bounds (e.g., maximum 100 items) guarantees smooth rendering.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Activity 1 (`MainActivity`)**:
   - Provide an `EditText` for integer count and a "Generate Items" button.
   - On button tap, validate input: verify it is not empty, numeric, > 0, and \(\le 100\).
   - Package count in `intent.putExtra(EXTRA_ITEM_COUNT, count)`.
   - Start `DynamicItemsActivity`.
2. **Activity 2 (`DynamicItemsActivity`)**:
   - Extract `int count = getIntent().getIntExtra(EXTRA_ITEM_COUNT, 0)`.
   - Clear existing views from the `LinearLayout` inside the `ScrollView`.
   - Loop \(i = 1\) to \(\text{count}\):
     - Instantiate a card `LinearLayout`.
     - Instantiate a circular badge `TextView` ("#i").
     - Instantiate a text column (title + description).
     - Instantiate an action `Button` with a click listener showing a Toast.
     - Add child views to card, and add card to the scroll container.
3. **Back Navigation**: Provide a Back button to finish the activity and return to input.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical11/MainActivity.java)
- [DynamicItemsActivity.java](file:///app/src/main/java/com/example/practical11/DynamicItemsActivity.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)
- [activity_dynamic_items.xml](file:///app/src/main/res/layout/activity_dynamic_items.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [item_badge.xml](file:///app/src/main/res/drawable/item_badge.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_11_Dynamic_Views` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or device.
4. Enter a count (e.g., `8`) and tap **Generate Items in Second Activity**.
5. Scroll through the generated list and click individual "Tap" buttons.

## 11. Expected Output
- `MainActivity` accepts input and validates it against bounds.
- `DynamicItemsActivity` opens smoothly, displaying the exact count of generated card items inside a scrollable container.
- Each item has its own badge ("#1", "#2"...), title, description, and interactive button.
- Clicking any item's "Tap" button triggers a Toast displaying that item's number.

## 12. Testing
| Test Case ID | Input Count | Expected Result | Status |
|---|---|---|---|
| TC_11_01 | `5` | Navigates to second activity; renders 5 cards | Pass |
| TC_11_02 | `0` or `-3` | Validation error: count must be > 0 | Pass |
| TC_11_03 | Empty Input | Error: "Please enter the number of items" | Pass |
| TC_11_04 | `150` | Error: Exceeds safety cap of 100 items | Pass |
| TC_11_05 | Card Click | Toast: "Clicked Item #X" displays correct index | Pass |

## 13. Common Errors and Solutions
- **Error**: Content cut off at bottom of screen.
  - **Solution**: Ensure dynamic view container is wrapped in a `<ScrollView>` with `android:fillViewport="true"`.
- **Error**: Views overlap or have wrong dimensions.
  - **Solution**: Always instantiate `LinearLayout.LayoutParams(width, height)` and call `view.setLayoutParams(params)`.
- **Error**: UI stuttering or OutOfMemory when entering large numbers.
  - **Solution**: For thousands of items, use a `RecyclerView`. For direct dynamic layout generation, enforce a reasonable safety limit.

## 14. Viva Questions and Answers
1. **Q: How are UI views created dynamically in Java without XML?**
   - **A**: By instantiating view classes with a Context (e.g., `TextView tv = new TextView(this)`), configuring properties via setters (`setText()`, `setTextColor()`), applying `LayoutParams`, and adding them to a parent container via `parent.addView(tv)`.
2. **Q: Why is `ScrollView` required for dynamic view generation?**
   - **A**: A standard `LinearLayout` does not scroll when its child views exceed physical screen height. `ScrollView` provides vertical scrolling for arbitrary view hierarchies.
3. **Q: Why must pixel values be calculated dynamically from dp?**
   - **A**: Screen pixel densities vary across devices (mdpi, hdpi, xhdpi, xxhdpi). A fixed pixel value of 10px would appear large on a low-density screen and tiny on a high-density screen. Converting dp using `dp * density` guarantees consistent physical sizing.
4. **Q: What is the purpose of `LayoutParams.weight` in LinearLayout?**
   - **A**: `weight` specifies how remaining excess space along the orientation axis should be allocated among child views proportionally.
5. **Q: When should dynamic view generation be preferred over RecyclerView?**
   - **A**: Dynamic view generation is simpler for small, fixed, or heterogeneous form sections (such as adding 2-5 dynamic phone input fields). For large, repeating lists with scrolling, `RecyclerView` is preferred because it recycles view holders to conserve memory.
