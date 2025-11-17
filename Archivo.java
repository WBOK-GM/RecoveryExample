package recovery.RecoveryExample;
public class Archivo {
    private String nombre;
    private String contenido;
    private boolean estaEliminado;

    public Archivo(String nombre) {
        this.nombre = nombre;
        this.contenido = "";
        this.estaEliminado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }

    @Override
    public String toString() {
        String estado = estaEliminado ? "(ELIMINADO)" : "(ACTIVO)";
        return String.format("-> [%s] '%s' | Contenido: '%s'", estado, nombre, contenido.substring(0, Math.min(20, contenido.length())) + "...");
    }
}