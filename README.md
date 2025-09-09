# BRIK

BRIK is a minimal Android app that helps you **block your phone for a set duration** — turning it into a "brick" so you can focus.
## Installation

You can install BRIK in two ways:

### Method 1: Download Release APK
- Go to the [Releases](../../releases) section of this repository  
- Download the latest APK file  
- Install it on your Android device  

### Method 2: Build from Source
- Clone this repository  
- Open the project in **Android Studio**  
- Build & run it on your device  


## Permissions

BRIK requires the following permissions to function properly:

- **Device Administrator**  
  This permission makes it harder for the user to bypass the block by force-stopping the app or killing its service. It ensures the blocking feature stays active and reliable.

- **Post Notifications**  
  Used to display ongoing status and reminders in the notification panel.

- **Draw Over Other Apps**  
  Required to start the blocking activity even when the app is in the background, ensuring the block cannot be bypassed.

## Contributing
Pull requests are welcome! For major changes, please open an issue first.  

## License
[MIT]
