$path = "c:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\resources\org\jscience\ui\i18n\messages_social_en.properties"
$content = Get-Content $path -Raw

# Backup
Copy-Item $path "$path.bak"

# Kurzweil
$content = $content -replace 'demo\.kurzweildemo\.name', 'demo.kurzweildemo.name' # No change, just safety
$content = $content -replace 'kurzweil\.milestone\.', 'demo.kurzweildemo.milestone.'
$content = $content -replace 'kurzweil\.label\.linear', 'demo.kurzweildemo.label.linear'
$content = $content -replace 'kurzweil\.label\.log', 'demo.kurzweildemo.label.log'
$content = $content -replace 'kurzweil\.sidebar\.clock', 'demo.kurzweildemo.label.clock'
$content = $content -replace 'kurzweil\.sidebar\.linear', 'demo.kurzweildemo.label.linearclock'
$content = $content -replace 'kurzweil\.sidebar\.kurzweil', 'demo.kurzweildemo.label.kurzweilclock'
$content = $content -replace 'kurzweil\.sidebar\.accel', 'demo.kurzweildemo.label.acceleration'
$content = $content -replace 'kurzweil\.sidebar\.base', 'demo.kurzweildemo.label.basespeed'
$content = $content -replace 'kurzweil\.btn\.reset', 'demo.kurzweildemo.button.reset'
$content = $content -replace 'kurzweil\.btn\.pause', 'demo.kurzweildemo.button.pause'
$content = $content -replace 'generated\.kurzweil\.000', 'demo.kurzweildemo.text.placeholder.zero'

# Timeline Viewer
$content = $content -replace 'viewer\.timeline\.name', 'viewer.timelineviewer.name'
$content = $content -replace 'viewer\.timeline\.desc', 'viewer.timelineviewer.desc'
$content = $content -replace 'viewer\.timeline\.param\.', 'viewer.timelineviewer.param.'
$content = $content -replace 'viewer\.timeline\.default\.', 'viewer.timelineviewer.default.'

# Cleanup old title/desc (lines 36-39) if they exist and duplicate
# But I am not deleting lines, just renaming.
# demo.kurzweildemo.name exists in line 485.
# kurzweil.title (line 36) -> if I leave it, it's unused.
# I'll just leave unused keys for now to avoid accidental deletion of useful text.

Set-Content $path $content
Write-Host "Social properties updated."
