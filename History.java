package recovery.RecoveryExample;

import java.util.ArrayList;
import java.util.List;

public class History {
    private List<String> commandHistory;

    public History() {
        this.commandHistory = new ArrayList<>();
    }

    /**
     * Añade un comando al historial.
     */
    public void add(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }
        this.commandHistory.add(command);
    }

    /**
     * Muestra todos los comandos en el historial con un número.
     */
    public void display() {
        if (commandHistory.isEmpty()) {
            System.out.println("No hay comandos en el historial.");
            return;
        }
        System.out.println("--- Historial de Comandos ---");
        for (int i = 0; i < commandHistory.size(); i++) {
            // Imprimimos con base 1 para que sea más natural para el usuario
            System.out.printf("  %d: %s\n", i + 1, commandHistory.get(i));
        }
        System.out.println("-----------------------------");
    }
    
    /**
     * Obtiene el último comando ejecutado.
     * Itera hacia atrás para saltarse el '!!' si se usó varias veces.
     */
    public String getLastCommand() {
        if (commandHistory.isEmpty()) {
            return null;
        }
        // Itera hacia atrás para encontrar el último comando real (no '!!')
        for (int i = commandHistory.size() - 1; i >= 0; i--) {
            String cmd = commandHistory.get(i);
            if (!cmd.equals("!!")) {
                return cmd;
            }
        }
        return null; // No se encontró ningún comando real
    }
}