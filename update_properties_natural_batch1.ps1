$msgPath = "c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\resources\org\jscience\ui\i18n\messages_natural_en.properties"

if (Test-Path $msgPath) {
    $msg = Get-Content $msgPath -Raw
    $newKeys = @{
        "reader.chemistrydatareader.name" = "Chemistry Data Reader"
        "reader.chemistrydatareader.desc" = "Generic Chemistry Data Reader (JSON)."
        "reader.chemistrydatareader.longdesc" = "Reads chemistry data including elements and molecules from JSON resources."
        
        "reader.fastareader.name" = "FASTA Reader"
        "reader.fastareader.desc" = "FASTA Sequence Reader."
        "reader.fastareader.longdesc" = "Reads biological sequences from FASTA files."
        
        "writer.fastawriter.name" = "FASTA Writer"
        "writer.fastawriter.desc" = "Writes FASTA format files."
        "writer.fastawriter.longdesc" = "Writes biological sequences to FASTA format files."
        
        "reader.pdbreader.name" = "PDB Reader"
        "reader.pdbreader.desc" = "Protein Data Bank (PDB) Reader."
        "reader.pdbreader.longdesc" = "Reads protein structures from PDB format files or RCSB PDB API."
        
        "writer.pdbwriter.name" = "PDB Writer"
        "writer.pdbwriter.desc" = "Writes protein structures to PDB format."
        "writer.pdbwriter.longdesc" = "Exports protein structure data to standard PDB file format."
    }

    foreach ($key in $newKeys.Keys) {
        if ($msg -notmatch [regex]::Escape($key) + "=") {
            $msg += "`n$key=" + $newKeys[$key]
            Write-Host "Added $key"
        }
    }
    Set-Content -Path $msgPath -Value $msg
    Write-Host "Updated messages_natural_en.properties"
} else {
    Write-Host "Messages file not found: $msgPath"
}
