package recovery.RecoveryExample.componentes;


public class DirectorioEntry {
    private String nombre;
    private int inodoId;

    public DirectorioEntry(String nombre, int inodoId) {
        this.nombre = nombre;
        this.inodoId = inodoId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getInodoId() {
        return inodoId;
    }

    @Override
    public String toString() {
        return String.format("Nombre: '%s' -> (apunta a Inodo #%d)", nombre, inodoId);
    }
}