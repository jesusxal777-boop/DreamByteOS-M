# DreamByte OS M

DreamByte OS M is the Android-based mobile operating system layer for DreamPhones and DreamTabs. This repository starts the **0.1 Prototype**: a real, compilable Android app rather than a static mockup.

## Included

- DreamByte launcher surface with Modern and Retro modes
- Functional in-app DreamByte Terminal / controlled DreamShell
- Explicit `pkg` command boundary for the DreamByte Package Repository
- Service and AOSP integration seams reserved for later phases
- GitHub Actions build, test, and debug APK artifact

## Scope guardrails

This prototype does not claim root, replace Android SystemUI, modify partitions, silently install APKs, or pretend that Python/wget/curl/git are installed. See [`docs/architecture.md`](docs/architecture.md) and [`docs/roadmap.md`](docs/roadmap.md).

Reference concepts: [DreamByte-Launcher](https://github.com/jesusxal777-boop/DreamByte-Launcher).
