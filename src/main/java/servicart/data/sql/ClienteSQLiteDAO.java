package servicart.data.sql;

import servicart.models.entidades.Cliente;
import servicart.data.interfaces.CrudDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteSQLiteDAO implements CrudDAO<Cliente> {

    @Override
    public void save(Cliente cliente) {
        String sql = "INSERT INTO Clientes (cedula, nombre, email, celular, activo) VALUES (?, ?, ?, ?, 1)";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getCelular());

            stmt.executeUpdate();
            System.out.println("Usuario guardado con éxito");
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    @Override
    public Optional<Cliente> findId(String cedula) {
        String sql = "SELECT * FROM Clientes WHERE cedula = ? AND activo = 1";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cedula);

            //El ResultSet actúa como un puntero sobre las filas devueltas
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { //Sí encuentra una coincidencia
                    Cliente cliente = new Cliente(cedula, rs.getString("nombre"), rs.getString("email"), rs.getString("celular"));

                    return Optional.of(cliente);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE activo = 1";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorremos todas las filas devueltas por la consulta
            while (rs.next()) {
                Cliente cliente = new Cliente(rs.getString("cedula"),  rs.getString("nombre"), rs.getString("celular"), rs.getString("email"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public void update(Cliente cliente) {
        String sql = "UPDATE Clientes SET nombre = ?, email = ?, celular = ?, activo = ? WHERE cedula = ?";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCelular());
            stmt.setInt(4, cliente.getActivo());
            stmt.setString(5, cliente.getCedula());

            stmt.executeUpdate();
            System.out.println("Usuario actualizado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void delete(String cedula) {
        //Eliminación lógica
        String sql = "UPDATE Clientes SET activo = 0 WHERE cedula = ?";

        try (Connection conn = ConexionSQLite.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cedula);

            stmt.executeUpdate();
            System.out.println("Usuario desactivado lógicamente con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al dar de baja al usuario: " + e.getMessage());
        }
    }
}