# Clases-2019c1
Prácticas de la materia

## Práctica Permisos y Persistencia

Configurar aplicación para que solicite permisos de lectura/escritura de almacenamiento externo y para que persista archivos en File System así como en preferencias.
* Partiendo de una aplicación de edición de fotos casi funcionando, se debe agregar el código necesario para que la misma sea funcional.
* La aplicación está preparada para:
  - Listar las imágenes editadas por el usuario previamente en la pantalla inicial de la misma, en formato de grilla o lista, según gusto de usuario (dicho gusto debe ser recordado por la aplicación -> Guardado en preferencias)
  - Lanzar un Picker de imágenes, y recibir la imágen seleccionada
  - Editar la imágen seleccionada según una serie de filtros y calibraciones de brillo, contraste y saturación
* Inicialmente, para probar la aplicacion, se puede ir a configuración y darle permisos al almacenamiento de forma manual. Al final de la práctica, la aplicación deberá poder pedir estos permisos por sí misma, y además, si dichos permisos son revocados en cualquier momento (desde la configuración del dispositivo), deberá ser capaz de notarlo y volver a pedirlos.
* A la aplicación le hace falta:
  - Pedir los permisos necesarios para poder leer/escribir en almacenamiento externo
  - Obtener la lista de imágenes editadas previamente por el usuario
  - Grabar en almacenamiento externo una imagen editada


### Pedir los permisos necesarios para poder leer/escribir en almacenamiento externo
* Agregar en `AndroidManifest.xml` el permiso necesario para acceder a Internet (en `<manifest>`, fuera de `<application>`):
```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
* Agregar en el ActivityMain el OnRequestPermissionResult para gestionar el retorno del pedido de permisos
```
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
        Permissions.REQUEST_WRITE_EXTERNAL_STORAGE -> {
            if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(launchImagePickerPending) {
                    launchImagePicker()
                }
                if(showImagesListPending) {
                    showImagesFragment()
                }
            } else {
                launchImagePickerPending = false
                showImagesListPending = false
            }
            return
        }
    }

    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
```
* Agregar ```Permissions.checkForPermissions.......``` antes de mostrar el ImagesFragment
* Agregar ```Permissions.checkForPermissions.......``` antes de lanzar el ImagePicker

### Obtener la lista de imágenes editadas previamente por el usuario
* Utilizar ```ExternalStorage.getFiles()``` para obtener la lista de imagenes editadas previamente

### Grabar en almacenamiento externo una imagen editada
* Utilizar ```ExternalStorage.saveFile``` para guardar la imagen

