$PROJECT_ID = "project-5ab36d37-3e39-413e-a07"
$VM_NAME = "episteme-gpu-test-vm"
$ZONE = "us-east4-c"
$RETRY_INTERVAL_SECONDS = 1800 # 30 minutes
$LOG_FILE = "c:\Silvere\Encours\Developpement\Episteme\scratch\gcp_retry_log.txt"

function Log-Message($Message, $Color = "White") {
    $timestampedMessage = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss'): $Message"
    Write-Host $timestampedMessage -ForegroundColor $Color
    $timestampedMessage | Out-File -FilePath $LOG_FILE -Append -Encoding utf8
}

Log-Message "Starting GCP VM Retry Loop for $VM_NAME in $ZONE..." "Cyan"
Log-Message "Retrying every $($RETRY_INTERVAL_SECONDS / 60) minutes." "Cyan"

while ($true) {
    Log-Message "Attempting to start $VM_NAME..."
    
    # Run gcloud and capture both output and error
    $startResult = gcloud compute instances start $VM_NAME --zone $ZONE --project $PROJECT_ID 2>&1
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -eq 0) {
        Log-Message "SUCCESS! $VM_NAME is now running." "Green"
        Log-Message "You can now run: tmp/gcp/start-gcp-gpu-vm.ps1 to SSH into it." "Green"
        break
    } else {
        $errorStr = [string]$startResult
        if ($errorStr -like "*ZONE_RESOURCE_POOL_EXHAUSTED*") {
            Log-Message "Resources exhausted in $ZONE. Waiting to retry..." "Yellow"
        } else {
            Log-Message "FAILED with error code $exitCode. Error message: $errorStr" "Red"
        }
    }
    
    Log-Message "Waiting $($RETRY_INTERVAL_SECONDS / 60) minutes before next attempt..." "Gray"
    Start-Sleep -Seconds $RETRY_INTERVAL_SECONDS
}
