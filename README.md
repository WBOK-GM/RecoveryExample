# RecoveryExample

Simulador forense de sistema de archivos que demuestra cómo funcionan los mecanismos de recuperación de archivos eliminados en sistemas tipo Unix/Linux mediante el uso de inodos y tablas de directorios.

## Descripción

Este proyecto es una implementación educativa en Java que simula el comportamiento de un sistema de archivos basado en inodos, similar al usado en sistemas ext2/ext3/ext4 de Linux. El simulador permite crear, escribir, eliminar y **recuperar** archivos para comprender cómo los datos "eliminados" permanecen en el disco hasta que son sobrescritos.

### Conceptos Clave

- **Inodo**: Estructura de datos que almacena la información y contenido de un archivo en el disco
- **DirectorioEntry**: Enlace que asocia un nombre de archivo con su inodo correspondiente
- **Link Count**: Contador de referencias que determina si un archivo está "eliminado" (0 enlaces) o activo (≥1 enlaces)
- **Recuperación Forense**: Proceso de re-enlazar inodos huérfanos (link count = 0) para restaurar archivos eliminados

## Arquitectura del Proyecto

### Componentes Principales

#### `Inodo.java`
Representa un archivo en el disco con:
- ID único
- Contenido del archivo
- Contador de enlaces (linkCount)

Cuando el `linkCount` llega a 0, el archivo se considera "eliminado" pero **los datos persisten** en memoria.

#### `DirectorioEntry.java`
Entrada de directorio que vincula:
- Nombre del archivo (visible para el usuario)
- ID del inodo al que apunta

#### `DiscoDuro.java`
Gestión del sistema de archivos simulado:
- Tabla de inodos (almacenamiento real)
- Directorio raíz (índice de archivos activos)
- Operaciones de archivos (touch, write, rm, recovery)

#### `History.java`
Sistema de historial de comandos similar a bash:
- Almacena todos los comandos ejecutados
- Soporta repetición del último comando con `!!`

#### `Simulador.java`
Interfaz de línea de comandos (CLI) tipo shell Unix.

## Instalación y Uso

### Requisitos

- Java JDK 8 o superior

### Compilación

```
javac recovery/RecoveryExample/*.java
```

### Ejecución

```
java recovery.RecoveryExample.Simulador
```

## Comandos Disponibles

| Comando | Descripción | Ejemplo |
|---------|-------------|---------|
| `touch <archivo>` | Crea un archivo vacío | `touch datos.txt` |
| `write <archivo>` | Escribe contenido en un archivo existente | `write datos.txt` |
| `ls` | Lista los archivos activos en el directorio | `ls` |
| `rm <archivo>` | "Elimina" un archivo (marca el inodo como libre) | `rm datos.txt` |
| `recovery` | Recupera todos los archivos eliminados | `recovery` |
| `debug_disk_view` | Muestra el estado forense completo del disco | `debug_disk_view` |
| `history` | Muestra el historial de comandos ejecutados | `history` |
| `clear` | Limpia la consola | `history` |
| `!!` | Re-ejecuta el último comando | `!!` |
| `exit` | Termina el simulador | `exit` |

## Ejemplo de Uso

```
mini-linux> touch secreto.txt
Archivo secreto.txt creado (Inodo #1).

mini-linux> write secreto.txt
Escribe el contenido para 'secreto.txt'. Presiona ENTER para guardar:
Información confidencial
Contenido guardado en 'secreto.txt'.

mini-linux> ls
secreto.txt

mini-linux> rm secreto.txt
Archivo 'secreto.txt' desenlazado (Inodo #1 ahora tiene 0 enlaces).

mini-linux> ls
(Directorio vacío)

mini-linux> recovery
Iniciando escaneo forense de la tabla de inodos...
Buscando inodos con 0 enlaces (sin referencia de directorio)...
... Inodo #1 encontrado (Estado: LIBRE).
    -> Éxito: Inodo #1 ha sido re-enlazado como 'recuperado_inodo_1.txt'.

--- Escaneo Finalizado ---
Total de archivos restaurados (re-enlazados): 1

mini-linux> ls
recuperado_inodo_1.txt
```
## Manejo de Errores y Robustez

El simulador está diseñado para ser robusto y fácil de usar, manejando errores de entrada comunes:

### 1. Comandos Incompletos

La clase Simulador.java verifica si los comandos que requieren un argumento (como touch o rm) lo han recibido. Si un usuario escribe solo touch, el programa no intenta ejecutar la acción (lo que causaría un error).

En su lugar, detecta que el argumento está vacío e imprime un mensaje de ayuda:

```mini-linux> rm
Uso: rm <nombre_archivo>
```

### 2. Errores Inesperados (Try-Catch)

Todo el switch de comandos dentro de Simulador.java está envuelto en un bloque try-catch (Exception e). Esta es una "red de seguridad" que previene que el simulador se cierre inesperadamente si ocurre un bug o un NullPointerException en las clases internas.

Si ocurre un error inesperado, el simulador imprimirá el error en la consola pero no se detendrá. El usuario verá el prompt mini-linux> de nuevo y podrá seguir trabajando.

## Funcionamiento de la Recuperación

### ¿Por qué funciona la recuperación?

1. **Eliminación lógica vs física**: El comando `rm` solo elimina la entrada del directorio y decrementa el `linkCount` del inodo
2. **Persistencia de datos**: El contenido del inodo permanece intacto en la tabla de inodos
3. **Re-enlazado**: El comando `recovery` escanea todos los inodos con `linkCount == 0` y crea nuevas entradas de directorio para ellos

### Limitaciones (Realismo del Simulador)

- En sistemas reales, los inodos libres pueden ser sobrescritos por nuevos archivos
- Este simulador **no** sobrescribe inodos, permitiendo recuperación 100% confiable
- No simula fragmentación ni bloques de datos


## Estructura del Código

```
RecoveryExample/
├── componentes/
│   ├── DirectorioEntry.java    # Entrada de directorio
│   ├── DiscoDuro.java           # Núcleo del sistema de archivos
│   ├── History.java             # Historial de comandos
│   └── Inodo.java               # Estructura de inodo
├── Simulador.java               # Interfaz de usuario (main)
└── README.md                    # Este archivo
```
