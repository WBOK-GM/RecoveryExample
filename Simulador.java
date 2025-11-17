package recovery.RecoveryExample;

import java.util.Scanner;

public class Simulador {

public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DiscoDuro disco = new DiscoDuro();

        System.out.println("=== Simulador Forense de Sistema de Archivos (Java) ===");
        System.out.println("Comandos disponibles:");
        System.out.println("  touch <archivo>   - Crea un archivo vacío.");
        System.out.println("  write <archivo>   - Escribe contenido en un archivo.");
        System.out.println("  ls                - Lista los archivos activos.");
        System.out.println("  rm <archivo>      - 'Elimina' un archivo (lo marca).");
        System.out.println("  recovery          - Recupera todos los archivos 'eliminados'.");
        System.out.println("  debug_disk_view   - (Forense) Muestra el estado real del disco.");
        System.out.println("  exit              - Termina el simulador.");
        System.out.println("=========================================================");

        while (true) {
            System.out.print("\nmini-linux> ");
            String linea = scanner.nextLine().trim();

            if (linea.isEmpty()) {
                continue;
            }

         
            String[] partes = linea.split(" ", 2);
            String comando = partes[0].toLowerCase();
            String argumento = (partes.length > 1) ? partes[1] : "";

            try {
                switch (comando) {
                    case "touch":
                        if (argumento.isEmpty()) {
                            System.out.println("Uso: touch <nombre_archivo>");
                        } else {
                            disco.touch(argumento);
                        }
                        break;

                    case "write":
                        if (argumento.isEmpty()) {
                            System.out.println("Uso: write <nombre_archivo>");
                        } else {
                            System.out.println("Escribe el contenido para '" + argumento + "'. Presiona ENTER para guardar:");
                            String contenido = scanner.nextLine();
                            disco.write(argumento, contenido);
                        }
                        break;

                    case "ls":
                        disco.ls();
                        break;

                    case "rm":
                        if (argumento.isEmpty()) {
                            System.out.println("Uso: rm <nombre_archivo>");
                        } else {
                            disco.rm(argumento);
                        }
                        break;

                    case "recovery":
                        disco.recovery();
                        break;
                    
                    case "debug_disk_view":
                        disco.debugDiskView();
                        break;

                    case "exit":
                        System.out.println("Saliendo del simulador...");
                        scanner.close();
                        return; 

                    default:
                        if (!comando.equals("!!")) {
                            System.out.println("Comando desconocido: '" + comando + "'");
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}