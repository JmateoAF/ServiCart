package servicart.data.bin;

import servicart.data.interfaces.CrudDAO;
import servicart.models.entidades.Contrato;
import servicart.models.enums.CausaTerminacion;

import java.time.LocalDateTime;
import java.util.List;

public class ContratoBinarioDAO extends GenericBinarioDAO<Contrato> implements CrudDAO<Contrato>  {
    public ContratoBinarioDAO() {
        super("bin/contrato.bin");               // archivo único para esta entidad
    }

    @Override
    protected String getId(Contrato entidad) {
        return String.valueOf(entidad.getId());          // identificador natural
    }

    @Override
    protected boolean isActivo(Contrato entidad) {
        return entidad.getCausaTerminacion() == CausaTerminacion.ACTIVO;     // borrado lógico: activo = 1
    }

    /**
     * Borrado lógico: marca el campo activo a 0 en lugar de eliminar físicamente.
     * Si ya estaba inactivo, no hace nada.
     */
    public void terminarContrato(String id, CausaTerminacion causa) {
        List<Contrato> lista = (cache != null) ? cache : leerTodos();
        for (Contrato c : lista) {
            if (String.valueOf(c.getId()).equals(id)) {
                if (!isActivo(c)){
                    cache = lista;
                    return;
                }
                c.setCausaTerminacion(causa);       // CLIENTE o EMPRESA
                c.setFechaFin(fechaActual());
                guardarTodos(lista);
                cache = lista;
                return;
            }
            }
        throw new RuntimeException("Contrato no encontrado");
    }

    private LocalDateTime fechaActual() {
        return LocalDateTime.now();
    }
}
