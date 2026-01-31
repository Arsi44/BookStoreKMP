### Android

#### Запуск эмулятора

```powershell

# Запустить FAST APi
cd "C:\Users\arsi44\Desktop\Projects with potential\analyst-lab"
.\venv\Scripts\Activate.ps1 
python -m src.main

# Запустить эмулятор (например Pixel_9)
& "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe" -avd Pixel_9

# собрать приложение
 cd "C:\Users\arsi44\Desktop\Projects with potential\analyst-lab\BookStoreKMP"
.\gradlew.bat :androidapp:installDebug

# Запустить приложение
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" shell am start -n com.analystlab.app/.MainActivity

# Логи в реальном времени
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" logcat

\
```