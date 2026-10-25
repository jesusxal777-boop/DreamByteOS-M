#!/usr/bin/env bash
set -u

# DreamByte OS M environment validator.
# This script reports capability; it does not install packages or modify the host.

if [[ -n "${BASH_VERSION:-}" ]]; then
  set -o pipefail
fi

pass=0
warn=0
fail=0

result() {
  local level="$1"; shift
  printf '[%s] %s\n' "$level" "$*"
  case "$level" in
    PASS) pass=$((pass + 1)) ;;
    WARN) warn=$((warn + 1)) ;;
    FAIL) fail=$((fail + 1)) ;;
  esac
}

have() { command -v "$1" >/dev/null 2>&1; }

printf '%s\n' 'DreamByte OS M environment validation'
printf 'timestamp: %s\n' "$(date -u +%Y-%m-%dT%H:%M:%SZ)"
printf 'host: %s\n' "$(hostname 2>/dev/null || printf unknown)"
printf 'kernel: %s\n' "$(uname -srmo 2>/dev/null || printf unknown)"
printf '%s\n' '--- platform ---'

arch="$(uname -m 2>/dev/null || printf unknown)"
if [[ "$arch" == "x86_64" ]]; then
  result PASS "Host architecture is x86_64"
elif [[ "$arch" == "aarch64" || "$arch" == "arm64" ]]; then
  result WARN "Host architecture is $arch; the planned x86_64 emulator/build path is not native"
else
  result WARN "Host architecture is $arch; compatibility is unverified"
fi

if have nproc; then
  cpus="$(nproc)"
  if (( cpus >= 8 )); then result PASS "CPU cores available: $cpus"; else result WARN "CPU cores available: $cpus; AOSP builds may be impractical"; fi
else
  result WARN 'nproc is unavailable; CPU count is unknown'
fi

if have free; then
  mem_kib="$(free -k | awk '/^Mem:/ {print $2}')"
  mem_gib=$((mem_kib / 1024 / 1024))
  if (( mem_gib >= 16 )); then result PASS "RAM available to the host: approximately ${mem_gib} GiB"; else result WARN "RAM available to the host: approximately ${mem_gib} GiB; AOSP builds commonly require more"; fi
else
  result WARN 'free is unavailable; memory is unknown'
fi

if have df; then
  avail_kib="$(df -Pk . | awk 'NR==2 {print $4}')"
  avail_gib=$((avail_kib / 1024 / 1024))
  if (( avail_gib >= 200 )); then result PASS "Workspace free disk: approximately ${avail_gib} GiB"; else result WARN "Workspace free disk: approximately ${avail_gib} GiB; a full AOSP checkout/build normally needs substantially more"; fi
fi

printf '%s\n' '--- virtualization ---'
if [[ -e /dev/kvm ]]; then
  result PASS '/dev/kvm exists; emulator acceleration may be available'
else
  result WARN '/dev/kvm is absent; hardware-accelerated Android Emulator execution is unavailable or unverified'
fi

printf '%s\n' '--- required tools ---'
for tool in git python3 java repo adb emulator sdkmanager; do
  if have "$tool"; then result PASS "$tool: $(command -v "$tool")"; else result WARN "$tool is not installed or not on PATH"; fi
done

printf '%s\n' '--- Android SDK ---'
if [[ -n "${ANDROID_HOME:-}" ]]; then
  result PASS "ANDROID_HOME is set: $ANDROID_HOME"
elif [[ -n "${ANDROID_SDK_ROOT:-}" ]]; then
  result PASS "ANDROID_SDK_ROOT is set: $ANDROID_SDK_ROOT"
else
  result WARN 'ANDROID_HOME/ANDROID_SDK_ROOT is not set'
fi

printf '%s\n' '--- summary ---'
printf 'pass=%d warn=%d fail=%d\n' "$pass" "$warn" "$fail"

# Capability warnings are expected on lightweight hosts. A hard failure is reserved
# for malformed execution, so CI can use this report before deciding whether to build.
if (( fail > 0 )); then exit 2; fi
exit 0
