package servicart.data.sqlite;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Abono;
import servicart.entities.Carrito;
import servicart.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoSQLiteDAO implements CrudDAO<Carrito> {
    private final ClienteSQLiteDAO clienteDAO = new ClienteSQLiteDAO();
    private final AbonoSQLiteDAO abonoDAO = new AbonoSQLiteDAO();

    @Override
    public void save(Carrito c) {
        String sql = "INSERT INTO Carrito (idCliente) VALUES (?)";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getCliente().getCedula());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        guardarAbonosDelCarrito(c);
    }

    @Override
    public Optional<Carrito> findId(String id) {
        String sql = "SELECT * FROM Carrito WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Carrito> findAll() {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM Carrito";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return lista;
    }

    // Actualiza el carrito y resincroniza su lista de abonos por completo (borra e inserta de nuevo)
    @Override
    public void update(Carrito c) {
        String sql = "UPDATE Carrito SET idCliente = ? WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, c.getCliente().getCedula());
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        limpiarAbonosDelCarrito(c.getId());
        guardarAbonosDelCarrito(c);
    }

    @Override
    public void delete(String id) {
        limpiarAbonosDelCarrito(Integer.parseInt(id));
        String sql = "DELETE FROM Carrito WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void guardarAbonosDelCarrito(Carrito c) {
        String sql = "INSERT INTO CarritoAbono (idCarrito, idAbono) VALUES (?, ?)";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            for (Abono abono : c.getAbonos()) {
                stmt.setInt(1, c.getId());
                stmt.setInt(2, abono.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void limpiarAbonosDelCarrito(int idCarrito) {
        String sql = "DELETE FROM CarritoAbono WHERE idCarrito = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCarrito);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Carrito mapear(ResultSet rs) throws SQLException {
        int idCarrito = rs.getInt("id");
        String cedula = rs.getString("idCliente");
        Cliente cliente = clienteDAO.findId(cedula)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado, cedula=" + cedula));

        Carrito carrito = new Carrito(cliente);
        carrito.setId(idCarrito);

        String sqlAbonos = "SELECT idAbono FROM CarritoAbono WHERE idCarrito = ?";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sqlAbonos)) {
            stmt.setInt(1, idCarrito);
            try (ResultSet rsAbonos = stmt.executeQuery()) {
                while (rsAbonos.next()) {
                    abonoDAO.findId(String.valueOf(rsAbonos.getInt("idAbono")))
                            .ifPresent(carrito::agregarAbono);
                }
            }
        }
        return carrito;
    }
}