# Getting an actual .apk

I can't compile one in this chat — no network/Android SDK here — but two
paths get you a real APK with no Android Studio GUI work:

## Option A: GitHub Actions (recommended, no local setup)
1. Push this project to a GitHub repo.
2. In the repo's Settings → Secrets → Actions, add a secret named
   `GOOGLE_SERVICES_JSON` containing the full contents of your real
   `google-services.json` (from the Firebase console).
3. Push to `main`, or run the "Build debug APK" workflow manually from the
   Actions tab.
4. Download the `ParentGuard-debug-apk` artifact when the run finishes —
   that's your `.apk`.
   (`.github/workflows/build-apk.yml` is already set up for this.)

## Option B: Your own machine
1. Install Android Studio (bundles the SDK + a JDK). Open this project
   folder — Studio will auto-generate the missing
   `gradle/wrapper/gradle-wrapper.jar` the first time it syncs.
2. Drop your real `google-services.json` into `app/`.
3. `Build > Build Bundle(s)/APK(s) > Build APK(s)`, or from a terminal:
   ```
   ./gradlew assembleDebug
   ```
4. The APK lands at `app/build/outputs/apk/debug/app-debug.apk`.

## Note on the wrapper jar
`gradle/wrapper/gradle-wrapper.jar` (a small binary) isn't included in this
zip since it can't be generated without network access. Both options above
sidestep that: Android Studio regenerates it automatically, and the GitHub
Actions workflow uses its own Gradle install instead of the wrapper.
