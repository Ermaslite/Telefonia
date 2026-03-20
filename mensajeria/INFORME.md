# Informe de Práctica: Respuesta Automática por SMS

## 1. Introducción
Esta aplicación tiene como objetivo automatizar la respuesta a llamadas entrantes mediante mensajes de texto (SMS). Es una herramienta útil para situaciones donde el usuario no puede atender llamadas de contactos específicos pero desea informar el motivo de su ausencia de manera inmediata.

## 2. Requerimientos
- Detectar llamadas entrantes en tiempo real.
- Comparar el número entrante con un número preconfigurado por el usuario.
- Enviar un SMS automático con un texto personalizado.
- Registro del receptor (BroadcastReceiver) en el manifiesto.
- Interfaz para configurar el número y el mensaje.

## 3. Implementación Técnica

### 3.1 Permisos
Se utilizaron los siguientes permisos en el `AndroidManifest.xml`:
- `READ_PHONE_STATE`: Para detectar cambios en el estado del teléfono.
- `READ_CALL_LOG`: Necesario en versiones modernas de Android para obtener el número de teléfono entrante.
- `SEND_SMS`: Para realizar el envío del mensaje de texto.

### 3.2 Componentes Principales
- **MainActivity**: Desarrollada con Jetpack Compose. Permite al usuario guardar el número de destino y el mensaje en `SharedPreferences`. Además, gestiona la solicitud de permisos dinámicos.
- **CallReceiver**: Un `BroadcastReceiver` registrado en el manifiesto que intercepta la acción `ACTION_PHONE_STATE_CHANGED`. Cuando detecta el estado `RINGING`, extrae el número entrante y, si coincide con el configurado, utiliza `SmsManager` para enviar la respuesta.

## 4. Pruebas Realizadas
1. Se verificó la solicitud de permisos al iniciar la app.
2. Se probó el almacenamiento persistente de la configuración.
3. Se realizaron pruebas de recepción de llamadas, confirmando el envío del SMS únicamente cuando el número coincide exactamente con el ingresado.

## 5. Conclusiones
La aplicación cumple con los requisitos académicos, demostrando el uso de receptores de difusión (Broadcast Receivers), gestión de permisos y almacenamiento ligero con SharedPreferences en Android.
