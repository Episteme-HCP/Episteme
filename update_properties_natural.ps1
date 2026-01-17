# Update messages_natural_en.properties with new I18n key nomenclature for SpectrographViewer
$file = "c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\resources\org\jscience\ui\i18n\messages_natural_en.properties"
$content = Get-Content $file -Raw -Encoding UTF8

# Define new keys to add
$newKeys = @"

# SpectrographViewer - Updated keys
viewer.spectrographviewer.name=Spectrograph Viewer
viewer.spectrographviewer.desc=Real-time frequency analysis visualization.
viewer.spectrographviewer.longdesc=Visualizes sound or signal frequencies over time using a spectrogram and spectrum analyzer.
viewer.spectrographviewer.param.sensitivity=Sensitivity
viewer.spectrographviewer.param.sensitivity.desc=Adjusts the signal responsiveness
viewer.spectrographviewer.param.bands=Frequency Bands
viewer.spectrographviewer.param.bands.desc=Number of frequency bins to visualize
viewer.spectrographviewer.param.mode=Scientific Mode
viewer.spectrographviewer.param.mode.desc=Toggles between primitive and object-based engines
viewer.spectrographviewer.fps.label=FPS: --
viewer.spectrographviewer.fps.format=FPS: %.1f

# SpectrographDemo - Updated keys
demo.spectrographdemo.name=Spectrograph
demo.spectrographdemo.desc=Real-time frequency analysis visualization.
demo.spectrographdemo.longdesc=Visualizes sound or signal frequencies over time using a spectrogram and spectrum analyzer.
demo.spectrographdemo.label.source=Signal Source:
demo.spectrographdemo.source.voice=Voice (Synthetic)
demo.spectrographdemo.source.sine=Sine Wave (Synthetic)
demo.spectrographdemo.source.noise=White Noise
demo.spectrographdemo.source.custom=Custom WAV...
demo.spectrographdemo.btn.load=Load WAV File
"@

# Check if keys don't exist yet, then add them
if ($content -notmatch "viewer\.spectrographviewer\.name=") {
    $content = $content + $newKeys
    Set-Content $file $content -Encoding UTF8
    Write-Host "Added new SpectrographViewer and SpectrographDemo keys to properties file"
} else {
    Write-Host "Keys already exist, skipping"
}
