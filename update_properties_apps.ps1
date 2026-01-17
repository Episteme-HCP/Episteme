$path = "c:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\resources\org\jscience\apps\ui\i18n\messages_apps_en.properties"
$content = Get-Content $path -Raw

# Replace launcher keys
$content = $content -replace 'launcher\.title', 'app.launcher.name'
$content = $content -replace 'launcher\.stage\.title', 'app.launcher.header.killerapps' 
$content = $content -replace 'launcher\.desc\.', 'app.launcher.desc.'

Set-Content $path $content
Write-Host "Apps properties updated."
