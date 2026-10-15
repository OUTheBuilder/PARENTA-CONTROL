# ParentGuard (native Android, Kotlin)

Full rewrite of the earlier React Native prototype as a native Android app.
No JavaScript bridge — all screens, Firebase auth/data, and device-level
features (Device Admin lock, usage stats, foreground service, boot
receiver) are implemented directly in Kotlin.

## Before you build
1. **Rotate your Firebase key.** The key pasted into this project's source
   earlier is now exposed; generate a new one in the Firebase console.
2. Download your project's real `google-services.json` from
   Firebase Console → Project settings → Your apps, and put it at
   `app/google-services.json` (see `google-services.json.example`).
3. Open in Android Studio (Koala+), let Gradle sync, run on a device/emulator
   with API 26+.

## What's implemented
- Email/password auth + Firestore user profiles with a parent/child role (`data/FirebaseRepository.kt`)
- Role-based routing on launch (`MainActivity.kt`)
- Parent dashboard → SMS log, Screen Time, Safe Zones, Web History screens
- Child dashboard → guided, visible permission requests (no silent/hidden grants)
- Device Admin receiver + remote lock (`admin/`, `util/DeviceControlHelper.kt`)
- App usage stats via `UsageStatsManager` (`util/AppUsageHelper.kt`)
- Foreground `MonitoringService` with the mandatory persistent notification
- `BootReceiver` to restart the service after reboot
- Safe zones read from Firestore (`GeofenceActivity`) — wire up
  `GeofencingClient` in `MonitoringService` to actually fire enter/exit events

## Known gaps / things that need your decision
- **SMS**: `READ_SMS` is Play-Store-restricted to default SMS handlers. Either
  build the default-SMS-app flow, or distribute outside Play (enterprise/MDM).
- **Browser history**: there's no public Android API to read another browser's
  history on a non-rooted device. `BrowserHistoryActivity` reads from your own
  Firestore collection, which you'd populate via an in-app browser or a
  VpnService-based domain logger — see comments in that file.
- **Geofence monitoring**: the screen lists zones; add a `GeofencingClient`
  call in `MonitoringService.onStartCommand` to register real geofences and a
  `BroadcastReceiver` to handle `GeofencingEvent` transitions.
- No icons are bundled — drop `ic_launcher.png`/`.webp` into `res/mipmap-*`.

## Compliance note
Every "monitored" screen here assumes the device user knows the app is
installed and what it does (see the copy in `ChildDashboardActivity`). If
you intend covert/hidden operation instead, that's a different (and in many
places illegal) product — I didn't build toward that, and won't.
