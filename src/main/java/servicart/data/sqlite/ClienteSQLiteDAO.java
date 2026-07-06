package servicart.data.sqlite;

import servicart.entities.Cliente;
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
    public void save(Cliente c) {
        String sql = "INSERT INTO Clientes (cedula, nombre, email, celular, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, c.getCedula());
            stmt.setString(2, c.getNombre());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getCelular());
            stmt.setInt(5, c.getActivo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Cliente> findId(String cedula) {
        String sql = "SELECT * FROM Clientes WHERE cedula = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cedula);

            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return Optional.of(mapear(rs)); }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE activo = 1";  // ← corregido

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) clientes.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return clientes;
    }

    @Override
    public void update(Cliente c) {
        String sql = "UPDATE Clientes SET nombre = ?, email = ?, celular = ?, activo = ? WHERE cedula = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getCelular());
            stmt.setInt(4, c.getActivo());
            stmt.setString(5, c.getCedula());

            //if (stmt.executeUpdate() == 0)
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(String cedula) {
        //Eliminacion logica
        String sql = "UPDATE Clientes SET activo = 0 WHERE cedula = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cedula);

            //if (stmt.executeUpdate() == 0)
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //Único punto de conversión ResultSet -> Cliente
    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(rs.getString("cedula"), rs.getString("nombre"), rs.getString("email"), rs.getString("celular"), rs.getInt("activo"));
    }
}