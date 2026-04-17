# Kill anything on port 8080
$pids = (Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue).OwningProcess | Where-Object { $_ -gt 0 }
if ($pids) {
    $pids | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }
    Write-Host "Killed existing process on port 8080"
}

# Load .env variables
Get-Content "$PSScriptRoot\.env" | ForEach-Object {
    if ($_ -match "^(.*?)=(.*)$") {
        [System.Environment]::SetEnvironmentVariable($Matches[1].Trim(), $Matches[2].Trim(), "Process")
    }
}

# Set Java 17
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Run
Set-Location $PSScriptRoot
mvn clean spring-boot:run -s settings.xml
