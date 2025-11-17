package recovery.RecoveryExample.componentes;


public class Inodo {
    private int id;
    private String contenido;
    private int linkCount; // Contador de referencias (enlaces)

    public Inodo(int id) {
        this.id = id;
        this.contenido = "";
        this.linkCount = 0; 
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getContenido() {
        return contenido;
    }

    public int getLinkCount() {
        return linkCount;
    }

    // Setters
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }
    
    public void incrementarLinkCount() {
        this.linkCount++;
    }

    public void decrementarLinkCount() {
        if (this.linkCount > 0) {
            this.linkCount--;
        }
    }

    @Override
    public String toString() {
        String estado = (linkCount > 0) ? "ENLAZADO" : "LIBRE (Eliminado)";
        return String.format("Inodo #%d | Enlaces: %d | Estado: %s | Contenido: '%.20s...'",
                id, linkCount, estado, contenido.isEmpty() ? "" : contenido);
    }
}
