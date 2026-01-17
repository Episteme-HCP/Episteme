$path = "c:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\resources\org\jscience\ui\i18n\messages_social_en.properties"
$content = Get-Content $path -Raw

# Architecture
$content = $content -replace 'ArchitectureStability\.title', 'demo.architecturestabilitydemo.name'
$content = $content -replace 'ArchitectureStability\.desc', 'demo.architecturestabilitydemo.desc'
$content = $content -replace 'arch\.stability\.btn\.add', 'demo.architecturestabilitydemo.button.add'
$content = $content -replace 'arch\.stability\.btn\.reset', 'demo.architecturestabilitydemo.button.reset'
$content = $content -replace 'arch\.stability\.label\.stable', 'demo.architecturestabilitydemo.label.stable'
$content = $content -replace 'arch\.stability\.label\.collapsed', 'demo.architecturestabilitydemo.label.collapsed'
$content = $content -replace 'arch\.stability\.label\.com', 'demo.architecturestabilitydemo.label.com'
# Add controls key if missing
if (-not ($content -match 'demo\.architecturestabilitydemo\.label\.controls')) {
    $content += "`ndemo.architecturestabilitydemo.label.controls=Stability Controls"
}

# Linguistics
$content = $content -replace 'LinguisticsWordFreq\.title', 'demo.linguisticswordfreqdemo.name'
$content = $content -replace 'LinguisticsWordFreq\.desc', 'demo.linguisticswordfreqdemo.desc'
$content = $content -replace 'ling\.freq\.', 'demo.linguisticswordfreqdemo.'

# Economics GDP
$content = $content -replace 'EconomicsGDP\.title', 'demo.economicsgdpdemo.name'
$content = $content -replace 'EconomicsGDP\.desc', 'demo.economicsgdpdemo.desc'
$content = $content -replace 'econ\.gdp\.', 'demo.economicsgdpdemo.'

# Sociology Network
$content = $content -replace 'SociologyNetwork\.title', 'demo.sociologynetworkdemo.name'
$content = $content -replace 'SociologyNetwork\.desc', 'demo.sociologynetworkdemo.desc'

# Politics Voting
$content = $content -replace 'PoliticsVoting\.title', 'demo.politicsvotingdemo.name'
$content = $content -replace 'PoliticsVoting\.desc', 'demo.politicsvotingdemo.desc'
$content = $content -replace 'pol\.voting\.', 'demo.politicsvotingdemo.'

Set-Content $path $content
Write-Host "Social properties batch update completed."
