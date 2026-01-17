$msgPath = "c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\resources\org\jscience\ui\i18n\messages_natural_en.properties"

if (Test-Path $msgPath) {
    $msg = Get-Content $msgPath -Raw
    $newKeys = @{
        "reader.gbiftaxonomyreader.name" = "GBIF Taxonomy Reader"
        "reader.gbiftaxonomyreader.desc" = "Connector to the Global Biodiversity Information Facility (GBIF) API."
        "reader.gbiftaxonomyreader.longdesc" = "Provides access to the GBIF taxonomy backbone, allowing search and retrieval of biological species data."
        
        "reader.ncbitaxonomyreader.name" = "NCBI Taxonomy Reader"
        "reader.ncbitaxonomyreader.desc" = "Detailed NCBI Taxonomy Database Reader."
        "reader.ncbitaxonomyreader.longdesc" = "Connects to NCBI Taxonomy API (E-Utils) to search and retrieve full species classification trees and metadata."
        
        "reader.fastqreader.name" = "FASTQ Reader"
        "reader.fastqreader.desc" = "Parsed FASTQ format files (includes quality scores)."
        "reader.fastqreader.longdesc" = "Reads biological sequences from FASTQ format, including quality scores."
        
        "reader.fastabiosequencereader.name" = "FASTA BioSequence Reader"
        "reader.fastabiosequencereader.desc" = "Parser for FASTA format files into BioSequences."
        "reader.fastabiosequencereader.longdesc" = "Reads FASTA format files and converts them into BioSequence objects for specific biological analysis."
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
