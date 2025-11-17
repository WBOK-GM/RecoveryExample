package recovery.RecoveryExample;
import java.util.ArrayList;
import java.util.List;


public class DiscoDuro {
    
    private List<Inodo> tablaDeInodos;
    private List<DirectorioEntry> directorioRaiz;
    
    private int proximoInodoId = 1; 

    public DiscoDuro() {
        this.tablaDeInodos = new ArrayList<>();
        this.directorioRaiz = new ArrayList<>();
    }

    /**
     * Busca una entrada de directorio por su nombre.
     */
    private DirectorioEntry findEntryByNombre(String nombre) {
        for (DirectorioEntry entry : directorioRaiz) {
            if (entry.getNombre().equals(nombre)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Busca un inodo en el disco por su ID.
     */
    private Inodo findInodoById(int id) {
        for (Inodo inodo : tablaDeInodos) {
            if (inodo.getId() == id) {
                return inodo;
            }
        }
        return null; 
    }

    /**
     * Comando touch: Crea un nuevo archivo vacío (Inodo + DirectorioEntry).
     */
    public void touch(String nombre) {
        if (findEntryByNombre(nombre) != null) {
            System.out.println("Error: El archivo '" + nombre + "' ya existe.");
            return;
        }

        // 1. Crear el Inodo (el archivo real)
        Inodo nuevoInodo = new Inodo(proximoInodoId++);
        nuevoInodo.incrementarLinkCount(); // El nuevo enlace contará
        tablaDeInodos.add(nuevoInodo);

        // 2. Crear la Entrada de Directorio (el enlace/nombre)
        DirectorioEntry nuevaEntry = new DirectorioEntry(nombre, nuevoInodo.getId());
        directorioRaiz.add(nuevaEntry);

        System.out.println("Archivo '" + nombre + "' creado (Inodo #" + nuevoInodo.getId() + ").");
    }

    /**
     * Comando ls: Lista solo las entradas del directorio.
     */
    public void ls() {
        if (directorioRaiz.isEmpty()) {
            System.out.println("(Directorio vacío)");
            return;
        }
        for (DirectorioEntry entry : directorioRaiz) {
            System.out.println(entry.getNombre());
        }
    }

    /**
     * Comando rm: "Elimina" un archivo (desenlazándolo).
     */
    public void rm(String nombre) {
        DirectorioEntry entry = findEntryByNombre(nombre);
        if (entry == null) {
            System.out.println("Error: El archivo '" + nombre + "' no existe.");
            return;
        }

        // 1. Encontrar el Inodo
        Inodo inodo = findInodoById(entry.getInodoId());

        // 2. Eliminar la entrada del directorio (el "enlace")
        directorioRaiz.remove(entry);
        
        // 3. Decrementar el contador de enlaces del Inodo
        if (inodo != null) {
            inodo.decrementarLinkCount();
            System.out.println("Archivo '" + nombre + "' desenlazado (Inodo #" + inodo.getId() + " ahora tiene " + inodo.getLinkCount() + " enlaces).");
        } else {
            System.out.println("Error: Inodo huérfano. Eliminando entrada de directorio."); // Caso de error
        }
    }

    /**
     * Comando write: Escribe contenido en un archivo.
     */
    public void write(String nombre, String contenido) {
        DirectorioEntry entry = findEntryByNombre(nombre);
        if (entry == null) {
            System.out.println("Error: El archivo '" + nombre + "' no existe.");
            return;
        }

        Inodo inodo = findInodoById(entry.getInodoId());
        if (inodo != null) {
            inodo.setContenido(contenido);
            System.out.println("Contenido guardado en '" + nombre + "'.");
        } else {
            System.out.println("Error fatal: El directorio apunta a un inodo que no existe.");
        }
    }

    /**
     * Comando recovery: Recupera *todos* los inodos con 0 enlaces.
     */
    public void recovery() {
        System.out.println("Iniciando escaneo forense de la tabla de inodos...");
        System.out.println("Buscando inodos con 0 enlaces (sin referencia de directorio)...");

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        int archivosRecuperados = 0;
        for (Inodo inodo : tablaDeInodos) {
            if (inodo.getLinkCount() == 0) {
                System.out.println("... Inodo #" + inodo.getId() + " encontrado (Estado: LIBRE).");
                try { Thread.sleep(250); } catch (InterruptedException e) {}

                // Generar un nuevo nombre para el archivo recuperado
                String nuevoNombre = String.format("recuperado_inodo_%d.txt", inodo.getId());
                
                // Verificar que este nuevo nombre no exista ya
                if (findEntryByNombre(nuevoNombre) != null) {
                    System.out.println("    -> Error: No se pudo recuperar el Inodo #" + inodo.getId() + " (el nombre de recuperación '" + nuevoNombre + "' ya existe).");
                    continue;
                }

                // 1. Crear la nueva entrada de directorio
                DirectorioEntry nuevaEntry = new DirectorioEntry(nuevoNombre, inodo.getId());
                directorioRaiz.add(nuevaEntry);
                
                // 2. Incrementar el contador de enlaces del Inodo
                inodo.incrementarLinkCount();
                
                System.out.println("    -> Éxito: Inodo #" + inodo.getId() + " ha sido re-enlazado como '" + nuevoNombre + "'.");
                archivosRecuperados++;
            }
        }
        
        System.out.println("\n--- Escaneo Finalizado ---");
        if (archivosRecuperados == 0) {
            System.out.println("No se encontraron inodos libres para recuperar.");
        } else {
            System.out.println("Total de archivos restaurados (re-enlazados): " + archivosRecuperados);
        }
    }

    /**
     * Comando de depuración: Muestra el estado real de todo el "disco".
     */
    public void debugDiskView() {
        System.out.println("\n--- VISTA FORENSE DEL DISCO ---");
        System.out.println("=================================");
        System.out.println("1. DIRECTORIO RAIZ (Lo que 'ls' ve)");
        System.out.println("---------------------------------");
        if (directorioRaiz.isEmpty()) {
            System.out.println("(Directorio vacío)");
        } else {
            for (DirectorioEntry entry : directorioRaiz) {
                System.out.println(entry);
            }
        }
        
        System.out.println("\n2. TABLA DE INODOS (El disco 'real')");
        System.out.println("-----------------------------------");
        if (tablaDeInodos.isEmpty()) {
            System.out.println("(Tabla de inodos vacía)");
        } else {
            for (Inodo inodo : tablaDeInodos) {
                System.out.println(inodo);
            }
        }
        System.out.println("--- FIN DE LA VISTA ---");
    }
}