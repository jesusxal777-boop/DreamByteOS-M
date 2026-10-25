# DreamByte OS M — Auditoría de viabilidad inicial

**Fase:** M0 — viabilidad y arquitectura  
**Fecha de auditoría:** 2026-10-25  
**Repositorio seleccionado:** [jesusxal777-boop/DreamByteOS-M](https://github.com/jesusxal777-boop/DreamByteOS-M)  
**Revisión auditada:** `d5865bc32b8387e63a23fb3b5f81ff0d5fa6e0fa`  
**Rama:** `main`

## Resumen ejecutivo

El repositorio `DreamByteOS-M` es el punto de partida adecuado porque contiene el prototipo Android relacionado directamente con el producto. Sin embargo, su propia documentación establece que el estado actual es una aplicación Android compilable, no una distribución AOSP ni un reemplazo de Android SystemUI. Debe conservarse como base de producto/prototipo, pero no debe presentarse como una ROM existente.

La siguiente transición técnica pendiente es añadir una integración AOSP separada y reproducible. No se ejecutará un checkout completo de AOSP en este sandbox sin resolver primero las limitaciones de arquitectura, almacenamiento, memoria, herramientas y virtualización.

## Repositorios relacionados auditados

| Repositorio | Resultado | Decisión |
|---|---|---|
| `DreamByteOS-M` | Prototipo Android/Gradle con launcher, terminal, servicios y límites explícitos | **Seleccionado** como repositorio principal |
| `DreamByteOS` | Sistema Linux experimental con rootfs, instalador y QEMU | No reutilizar como base Android |
| `dreambyte-os` | Proyecto Shell/ISO Linux | No reutilizar como base AOSP |
| `DreamByte-MicroCore-OS` | Kernel i386 mínimo propio bajo QEMU | No reutilizar; plataforma distinta |
| `DreamByte-Android-Studio` | Editor SwiftUI para proyectos Android | Herramienta separada, no base del sistema |
| `DreamByte-Launcher` | Launcher independiente | Reutilización futura solo tras revisión de compatibilidad |

## Estado del repositorio seleccionado

La revisión auditada contiene:

- Proyecto Gradle Android con módulo `app`.
- Directorios de reserva para `systemui`, `services`, `launcher`, `dreamshell` y `pkg`.
- Workflow Android para validación y artefacto APK.
- Documentación que declara expresamente que no reemplaza SystemUI, no modifica particiones y no es AOSP.
- Referencia tecnológica Android 12/12L para el prototipo, no Android 15 AOSP.

Por ello, el código existente se preserva. Las futuras modificaciones AOSP deben mantenerse separadas mediante overlays, parches, manifestos de producto y documentación específica.

## Capacidades observadas en el sandbox

| Capacidad | Resultado observado | Impacto |
|---|---|---|
| Arquitectura | `aarch64` | No coincide con el camino previsto x86_64 del emulador |
| CPU | 6 núcleos | Puede ser insuficiente para un build AOSP práctico |
| RAM | 7.8 GiB, sin swap | Por debajo de un perfil cómodo para AOSP |
| Disco libre | Aproximadamente 40 GiB | Insuficiente o muy limitado para checkout/build AOSP completo |
| `/dev/kvm` | Ausente | Aceleración de emulador no disponible o no verificada |
| `git` | Instalado | Disponible |
| `gh` | Instalado y autenticado | Repositorios accesibles en modo admin |
| `java` | Instalado | Versión exacta pendiente de validación Android |
| `repo` | No instalado | Sin herramienta AOSP de sincronización |
| Android SDK | No detectado | No se puede ejecutar el build APK/emulador localmente aún |
| `adb` | No instalado | No se puede verificar boot localmente |
| `emulator` | No instalado | No se puede arrancar un AVD localmente |
| `sdkmanager` | No instalado | No se puede preparar SDK localmente |
| Tailscale | No instalado | No se puede abrir laboratorio remoto desde este sandbox |
| Docker | No instalado | No se usará como supuesto de recuperación |

## Decisión de arquitectura provisional

1. Mantener `DreamByteOS-M` como repositorio principal del producto.
2. Preservar el prototipo APK y sus límites declarados.
3. Añadir documentación y scripts de validación antes de cualquier integración AOSP grande.
4. Colocar la futura integración AOSP en una estructura separada, preferentemente mediante un checkout externo o manifest de repositorios, sin copiar todo AOSP dentro del repositorio del producto.
5. Usar Android 15 como objetivo lógico inicial, pero fijar la rama exacta solo después de validar la disponibilidad de fuente, herramientas y un host adecuado.
6. No declarar compilación, arranque ni compatibilidad Android 15 hasta realizar esas pruebas.

## Próximo bloqueo técnico

El sandbox actual no es un candidato fiable para compilar y ejecutar una imagen AOSP Android 15: es ARM64, tiene 40 GiB libres, 7.8 GiB de RAM, no expone KVM y carece del SDK/`repo`/emulador. El siguiente paso debe ser uno de estos caminos, sin incurrir en costes automáticamente:

- Usar un host Linux x86_64 con más almacenamiento, memoria y KVM.
- Usar un runner self-hosted autorizado para el build y el emulador.
- Preparar únicamente la capa de integración, scripts y CI hasta que exista un host apropiado.
- Utilizar GitHub Actions solo para validaciones ligeras; no asumir que un runner hospedado puede completar un build AOSP completo.

## Evidencia y límites

Esta auditoría verificó acceso de lectura a los repositorios y clonó localmente `DreamByteOS-M`. No verificó una compilación AOSP, una imagen de sistema, un arranque de Android 15, una animación de boot ni una sesión remota. Esos estados permanecen pendientes.
