$path = "C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\resources\org\jscience\ui\theme.css"
$content = Get-Content $path -Raw
$map = @{
    "-fx-background-color" = "background"
    "-fx-font-family" = "font-family"
    "-fx-font-size" = "font-size"
    "-fx-font-weight" = "font-weight"
    "-fx-font-style" = "font-style"
    "-fx-padding" = "padding"
    "-fx-border-color" = "border-color"
    "-fx-border-width" = "border-width"
    "-fx-border-radius" = "border-radius"
    "-fx-background-radius" = "border-radius" 
    "-fx-text-fill" = "color"
}

$lines = Get-Content $path
$newLines = @()
foreach ($line in $lines) {
    $newLine = $line
    foreach ($fx in $map.Keys) {
        if ($line -match "$fx\s*:\s*([^;]+);") {
           $val = $matches[1]
           $std = $map[$fx]
           if (-not ($line -match "$std\s*:")) {
               $newLine = $newLine + " " + $std + ": " + $val + ";"
           }
        }
    }
    $newLines += $newLine
}
Set-Content $path $newLines -Encoding UTF8
Write-Host "CSS Fixed"
