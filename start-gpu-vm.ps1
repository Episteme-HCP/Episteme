# Episteme GPU VM Control Script (Run this LOCALLY on Windows)
# This script starts the VM and opens an SSH session.

$PROJECT_ID = "project-5ab36d37-3e39-413e-a07"
$VM_NAME = "episteme-gpu-test-vm"
$ZONE = "europe-west4-b"

Write-Host "--- [1/3] Ensuring GCloud Project ---" -ForegroundColor Cyan
gcloud config set project $PROJECT_ID

Write-Host "--- [2/3] Starting Instance: $VM_NAME ---" -ForegroundColor Cyan
gcloud compute instances start $VM_NAME --zone $ZONE

Write-Host "--- [3/3] Opening SSH Session ---" -ForegroundColor Cyan
Write-Host "Once inside the VM, run: ./setup-gpu-vm.sh" -ForegroundColor Yellow
gcloud compute ssh $VM_NAME --zone $ZONE
