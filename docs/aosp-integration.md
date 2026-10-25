# Integración AOSP de DreamByte OS M

## Decisión inicial

La base elegida es la rama `android15-release` del manifiesto oficial de AOSP:

- Manifiesto: `https://android.googlesource.com/platform/manifest`
- Rama: `android15-release`
- Commit observado el 2026-10-25: `255c6e39821c035f3213874f9bd980ce1b7056ad`
- Target provisional: `aosp_x86_64-userdebug`

La revisión y el target son una **configuración inicial**, no una afirmación de que el target exista en todos los puntos de la rama. Después de sincronizar un checkout válido se debe ejecutar `lunch` y registrar el target aceptado por esa revisión.

## Separación de fuentes

El repositorio DreamByteOS-M no contiene una copia de AOSP. La fuente upstream se mantiene en un checkout externo configurado por `AOSP_DIR`. Este repositorio contiene únicamente:

- Configuración de producto y documentación.
- Overlays en `product/overlays/`.
- Permisos y manifiestos en `product/permissions/`.
- Parches específicos separados por componente en `patches/`.
- Scripts de validación, sincronización y diagnóstico.

Esto evita duplicar decenas de gigabytes de fuente upstream y facilita actualizar o comparar revisiones.

## Secuencia de integración

1. Ejecutar `tools/validate-environment.sh`.
2. Copiar `config/aosp.env.example` como `config/aosp.env` fuera del control de versiones.
3. Revisar espacio, RAM, arquitectura, virtualización y disponibilidad de `repo`.
4. Ejecutar `AOSP_ALLOW_SYNC=1 tools/sync-aosp.sh` únicamente en un host adecuado.
5. Verificar el target con `lunch` dentro del checkout.
6. Aplicar overlays/patches de DreamByte en una rama o manifest reproducible.
7. Compilar el target.
8. Arrancar el emulador con la imagen real y ejecutar las pruebas de boot.

## Estado actual

El sandbox actual no satisface las condiciones para ejecutar el paso 4 con seguridad: es ARM64, dispone de unos 40 GiB libres, tiene 7.8 GiB de RAM, no expone `/dev/kvm` y no tiene instalada la herramienta `repo`. Por eso el script se niega a sincronizar por defecto.

No se debe afirmar todavía que existe una imagen Android 15, que el target compila o que DreamGlass UI está integrada en SystemUI.

## Regla para el target

`aosp_x86_64-userdebug` se mantiene como objetivo de investigación para un Android Emulator x86_64 clásico. Si el manifiesto seleccionado ofrece un target de Cuttlefish u otro nombre más apropiado, se debe actualizar esta configuración y documentar la razón antes de compilar.
