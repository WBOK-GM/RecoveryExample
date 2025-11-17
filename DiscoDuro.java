package recovery.RecoveryExample;
import java.util.ArrayList;
import java.util.List;


public class DiscoDuro {
    private List<Archivo> almacenamiento;

    public DiscoDuro() {
        this.almacenamiento = new ArrayList<>();
    }

    private Archivo buscarArchivoVisible(String nombre) {
        for (Archivo archivo : almacenamiento) {
            if (archivo.getNombre().equals(nombre) && !archivo.isEstaEliminado()) {
                return archivo;
            }
        }
        return null;
    }
    
    
    private boolean existeArchivoVisible(String nombre) {
        return buscarArchivoVisible(nombre) != null;
    }

   
    public void touch(String nombre) {
        if (existeArchivoVisible(nombre)) {
            System.out.println("Error: El archivo '" + nombre + "' ya existe.");
        } else {
            Archivo nuevoArchivo = new Archivo(nombre);
            almacenamiento.add(nuevoArchivo); // Se añade al "disco"
            System.out.println("Archivo '" + nombre + "' creado.");
        }
    }

    
    public void ls() {
        List<String> archivosVisibles = new ArrayList<>();
        for (Archivo archivo : almacenamiento) {
            if (!archivo.isEstaEliminado()) {
                archivosVisibles.add(archivo.getNombre());
            }
        }

        if (archivosVisibles.isEmpty()) {
            System.out.println("(Directorio vacío)");
        } else {
            for (String nombre : archivosVisibles) {
                System.out.println(nombre);
            }
        }
    }

  
    public void rm(String nombre) {
        Archivo archivo = buscarArchivoVisible(nombre);
        if (archivo == null) {
            System.out.println("Error: El archivo '" + nombre + "' no existe.");
        } else {
            // Simulación forense: no borramos el objeto, solo cambiamos la bandera.
            archivo.setEstaEliminado(true);
            System.out.println("Archivo '" + nombre + "' eliminado (marcado para sobrescribir).");
        }
    }

    
    public void write(String nombre, String contenido) {
        Archivo archivo = buscarArchivoVisible(nombre);
        if (archivo == null) {
            System.out.println("Error: El archivo '" + nombre + "' no existe.");
        } else {
            archivo.setContenido(contenido);
            System.out.println("Contenido guardado en '" + nombre + "'.");
        }
    }

   
    public void recovery() {
        System.out.println("Iniciando escaneo forense...");
        System.out.println("Buscando en los sectores de memoria archivos marcados como eliminados (sin un enlace)...");
        
        int archivosRecuperados = 0;
        int archivosEncontrados = 0;

        try {
            Thread.sleep(1500); // 500 ms de retraso
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Archivo archivo : almacenamiento) {
            if (archivo.isEstaEliminado()) {
                archivosEncontrados++;
                System.out.println("... Encontrado archivo '" + archivo.getNombre() + "' con estado ELIMINADO. Intentando recuperar...");
                
                try {
                    Thread.sleep(250); // 250 ms de retraso
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if(existeArchivoVisible(archivo.getNombre())) {
                     System.out.println("    -> Error: No se pudo recuperar '" + archivo.getNombre() + "' (ya existe un archivo activo con ese nombre).");
                } else {
                    archivo.setEstaEliminado(false);
                    System.out.println("    -> Éxito: Archivo '" + archivo.getNombre() + "' ha sido restaurado.");
                    archivosRecuperados++;
                }
            }
        }
        
        System.out.println("\n--- Escaneo Finalizado ---");
        if (archivosEncontrados == 0) {
            System.out.println("No se encontraron archivos marcados como eliminados en el disco.");
        } else {
            System.out.println("Total de archivos eliminados encontrados: " + archivosEncontrados);
            System.out.println("Total de archivos restaurados exitosamente: " + archivosRecuperados);
            System.out.println("¡Recuperación completada!");
        }
    }

    
    public void debugDiskView() {
        System.out.println("--- VISTA FORENSE DEL DISCO ---");
        if (almacenamiento.isEmpty()) {
            System.out.println("(Disco físico vacío)");
            return;
        }
        
        for (int i = 0; i < almacenamiento.size(); i++) {
            Archivo archivo = almacenamiento.get(i);
            System.out.printf("Bloque %d: %s\n", i, archivo.toString());
        }
        System.out.println("--- FIN DE LA VISTA ---");
    }
}