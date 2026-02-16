$files = Get-ChildItem -Path . -Recurse -Filter "*.java"
$groups = $files | Group-Object Name | Where-Object {$_.Count -gt 1}
foreach ($g in $groups) {
    Write-Host "Duplicate: $($g.Name)"
    foreach ($f in $g.Group) {
        Write-Host "  - $($f.FullName)"
    }
}
