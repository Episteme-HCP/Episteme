# Episteme Hugging Face Deployment Script (Industrial Version)
# This script creates a lean deployment branch and pushes it to Hugging Face.

$HF_REMOTE = "hf"
$DEPLOY_BRANCH = "hf-deployment"

Write-Host "--- Starting Episteme HF Deployment ---" -ForegroundColor Cyan

# 1. Ensure we are in a clean state and synced with GitHub
$currentBranch = git rev-parse --abbrev-ref HEAD
if ($null -eq $currentBranch) { Write-Error "Not in a git repo"; exit }
Write-Host "Syncing $currentBranch with GitHub..." -ForegroundColor Yellow
git push origin $currentBranch

# 2. Create or reset the deployment branch (Orphan to save space)
Write-Host "Creating lean deployment branch..." -ForegroundColor Yellow
if (git branch | Select-String $DEPLOY_BRANCH) {
    git branch -D $DEPLOY_BRANCH
}
git checkout --orphan $DEPLOY_BRANCH

# 3. Clear the index (it starts with all files from previous branch)
git rm -rf . --cached > $null

# 4. Add ONLY the essential modules (The Scientific Engine)
Write-Host "Adding essential modules..." -ForegroundColor Yellow
cp docker/Dockerfile.huggingface Dockerfile
git add Dockerfile
git add pom.xml
git add README.md
git add .gitignore
git add episteme-core/
git add episteme-server/
git add episteme-natural/
git add episteme-social/
git add episteme-native/
git add libs/mpj.jar

# Remove heavy 3D models rejected by Hugging Face
Write-Host "Pruning heavy 3D assets..." -ForegroundColor Yellow
git rm -rf --cached episteme-natural/src/main/resources/org/episteme/natural/medicine/anatomy/models/*.fbx 2>$null

# 5. Commit the lean version
Write-Host "Committing lean version..." -ForegroundColor Yellow
git commit -m "deploy: industrial lean build for Hugging Face Spaces"

# 6. Push to Hugging Face
Write-Host "Pushing to Hugging Face..." -ForegroundColor Green
git push $HF_REMOTE $DEPLOY_BRANCH:main --force

# 7. Cleanup and return to main branch
Write-Host "Cleaning up..." -ForegroundColor Yellow
git checkout $currentBranch
git branch -D $DEPLOY_BRANCH

Write-Host "--- Deployment Complete! ---" -ForegroundColor Green
Write-Host "Monitor your build at: https://huggingface.co/spaces/silveremartin/Episteme"
