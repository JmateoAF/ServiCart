package servicart.ui.core;

import servicart.models.entities.Carrito;
import servicart.models.entities.Cliente;
import servicart.dtos.ClienteDTO;
import servicart.dtos.ClienteMapper;

/* Estado de sesión compartido entre todos los controllers del cliente
Por qué es estático: en JavaFX cada controller se recrea al cargar la vista
No hay forma nativa de pasar objetos entre controllers sin un estado global
Sesion es ese estado global — controlado, en un solo lugar.

clienteDTO -> para mostrar datos en la UI (inmutable)
clienteEntity -> para operaciones de dominio (Carrito la necesita)
carrito -> el carrito activo del cliente */

public class Sesion {
    private static ClienteDTO clienteDTO;
    private static Cliente clienteEntity;
    private static Carrito carrito;

    //Inicia la sesión del cliente. Crea un carrito vacío
    public static void iniciar(Cliente cliente) {
        clienteEntity = cliente;
        clienteDTO = ClienteMapper.toDTO(cliente);
        carrito = new Carrito(cliente);
    }

    public static void cerrar() {
        clienteEntity = null;
        clienteDTO = null;
        carrito = null;
    }

    public static boolean hayCliente() { return clienteDTO != null; }
    public static ClienteDTO getClienteDTO() { return clienteDTO; }
    public static Cliente  getClienteEntity() { return clienteEntity; }
    public static Carrito  getCarrito() { return carrito; }
}
