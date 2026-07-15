package servicart.domain.services;

import servicart.data.FactoryDAO;

public class BdService {
    public static void configurarBaseDatos(String nombreBd) {
        FactoryDAO.configurar(nombreBd);
    }
}
