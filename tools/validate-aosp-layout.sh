#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
required=(
  "aosp/README.md"
  "config/aosp.env.example"
  "product/overlays"
  "product/permissions"
  "patches/systemui"
  "patches/framework"
  "patches/settings"
  "patches/launcher"
  "patches/bootanimation"
  "tools/sync-aosp.sh"
  "tools/validate-environment.sh"
)

missing=0
for path in "${required[@]}"; do
  if [[ -e "$ROOT_DIR/$path" ]]; then
    printf '[PASS] %s\n' "$path"
  else
    printf '[FAIL] %s\n' "$path"
    missing=$((missing + 1))
  fi
done

if (( missing > 0 )); then
  printf 'missing=%d\n' "$missing"
  exit 1
fi
printf 'missing=0\n'
