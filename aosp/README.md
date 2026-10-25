# AOSP workspace boundary

This directory records the boundary between the DreamByteOS-M product repository and an external AOSP checkout.

AOSP source is intentionally not vendored here. Use `config/aosp.env.example` and `tools/sync-aosp.sh` on a host that meets the documented requirements. Product overlays, permissions and DreamGlass-specific patches remain in this repository and are applied to the external checkout through a reproducible integration step.

The current sandbox has not synchronized AOSP and has not built or booted Android 15.
