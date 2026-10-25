#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CONFIG_FILE="${AOSP_CONFIG_FILE:-$ROOT_DIR/config/aosp.env}"

if [[ ! -f "$CONFIG_FILE" ]]; then
  echo "Missing configuration: $CONFIG_FILE" >&2
  echo "Copy config/aosp.env.example to config/aosp.env and review it." >&2
  exit 2
fi

# shellcheck disable=SC1090
source "$CONFIG_FILE"

: "${AOSP_MANIFEST_URL:?AOSP_MANIFEST_URL is required}"
: "${AOSP_BRANCH:?AOSP_BRANCH is required}"
: "${AOSP_DIR:?AOSP_DIR is required}"
: "${AOSP_JOBS:=4}"

if [[ "${AOSP_ALLOW_SYNC:-0}" != "1" ]]; then
  echo "Refusing to sync AOSP: set AOSP_ALLOW_SYNC=1 after reviewing the host and configuration." >&2
  exit 3
fi

if ! command -v repo >/dev/null 2>&1; then
  echo "The 'repo' tool is required but is not installed or not on PATH." >&2
  exit 4
fi

if [[ "$(uname -m)" != "x86_64" ]]; then
  echo "Warning: host architecture is $(uname -m), not x86_64; continue only if this is intentional." >&2
fi

mkdir -p "$AOSP_DIR"
if [[ -e "$AOSP_DIR/.repo/manifest.xml" ]]; then
  echo "Existing AOSP checkout detected: $AOSP_DIR"
else
  echo "Initializing AOSP checkout at $AOSP_DIR"
  repo init -u "$AOSP_MANIFEST_URL" -b "$AOSP_BRANCH" --depth=1
fi

if [[ -n "${AOSP_REVISION:-}" ]]; then
  echo "Configured manifest revision: $AOSP_REVISION"
  echo "The revision is recorded for provenance; repo sync will follow the selected branch unless the checkout is pinned separately."
fi

echo "Synchronizing AOSP with $AOSP_JOBS parallel jobs"
repo sync --current-branch --no-tags --optimized-fetch --prune -j"$AOSP_JOBS"

echo "AOSP sync completed: $AOSP_DIR"
