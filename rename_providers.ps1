$files = Get-ChildItem -Path . -Recurse -Filter "*BackendProvider.java"
foreach ($file in $files) {
    if ($file.FullName -like "*\target\*") { continue } # Skip target dir

    $oldName = $file.Name.Replace(".java", "")
    $newName = $oldName.Replace("BackendProvider", "Backend")
    $newPath = $file.FullName.Replace("BackendProvider.java", "Backend.java")
    
    Write-Host "Renaming $oldName to $newName"
    
    # Rename file
    Move-Item -Path $file.FullName -Destination $newPath -Force
    
    # Replace content in the file itself (Class declaration)
    (Get-Content $newPath) -replace $oldName, $newName | Set-Content $newPath
    
    # Replace usages in codebase
    $javaFiles = Get-ChildItem -Path . -Recurse -Filter "*.java"
    foreach ($javaFile in $javaFiles) {
        if ($javaFile.FullName -like "*\target\*") { continue }
        
        # Check if file contains old name
        if (Select-String -Path $javaFile.FullName -Pattern $oldName -SimpleMatch -Quiet) {
            Write-Host "  Updating references in $($javaFile.Name)"
            (Get-Content $javaFile.FullName) -replace $oldName, $newName | Set-Content $javaFile.FullName
        }
    }
}
Write-Host "Rename complete."
