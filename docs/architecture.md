# DreamByte OS M architecture

## Product boundary

- **DreamByte OS** → Linux → DreamPads / DreamStations.
- **DreamByte OS M** → Android-based → DreamPhones / DreamTabs.

DreamByte OS M 0.1 is an application layer over Android. Android 12/12L is the technology reference for the first prototype; this repository does not create a kernel, replace Android, or patch AOSP.

## Layer model

```text
Android 12/12L reference
        ↓
DreamByte OS M Layer
  ├── DreamByte Launcher (current app)
  ├── DreamByte Terminal / DreamShell (controlled interpreter)
  ├── DreamByte Package Manager (repository client boundary)
  ├── DreamByte Services (interfaces, not privileged services)
  └── DreamByte Apps
```

The current Android app intentionally uses only public Android APIs. It has no root access, does not modify system partitions, does not replace real SystemUI, and never claims that a package was installed when it was not.

## Future integration seams

The `systemui/`, `overlays/`, and `services/` directories reserve boundaries for future SystemUI overlays, framework modifications, privileged components, system services, and AOSP integrations. Those capabilities require an AOSP/device build and are explicitly out of scope for 0.1.
