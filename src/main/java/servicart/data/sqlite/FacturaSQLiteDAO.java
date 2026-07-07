package servicart.data.sqlite;

import servicart.data.FechaSQLite;
import servicart.data.interfaces.CrudDAO;
import servicart.entities.Contrato;
import servicart.entities.Factura;
import servicart.entities.enums.EstadoFactura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacturaSQLiteDAO implements CrudDAO<Factura> {
    private final ContratoSQLiteDAO contratoDAO = new ContratoSQLiteDAO();

    @Override
    public void save(Factura f) {
        String sql = "INSERT INTO Factura (fechaEmision, fechaVencimiento, fechaCorte, valorBase, valorTotal, estado, idContrato) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, FechaSQLite.formatear(f.getFechaEmision()));
            stmt.setString(2, FechaSQLite.formatear(f.getFechaVencimiento()));
            stmt.setString(3, FechaSQLite.formatear(f.getFechaCorte()));
            stmt.setDouble(4, f.getValorBase());
            stmt.setDouble(5, f.getValorTotal());
            stmt.setInt(6, f.getEstado().getCodigo());
            stmt.setInt(7, f.getContrato().getId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) { if (rs.next()) f.setId(rs.getInt(1)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<Factura> findId(String id) {
        String sql = "SELECT * FROM Factura WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return Optional.of(mapear(rs)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Factura> findAll() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM Factura";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Factura f) {
        String sql = "UPDATE Factura SET fechaEmision=?, fechaVencimiento=?, fechaCorte=?, valorTotal=?, estado=?, idContrato=? WHERE id=?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, FechaSQLite.formatear(f.getFechaEmision()));
            stmt.setString(2, FechaSQLite.formatear(f.getFechaVencimiento()));
            stmt.setString(3, FechaSQLite.formatear(f.getFechaCorte()));
            stmt.setDouble(4, f.getValorTotal());
            stmt.setInt(5, f.getEstado().getCodigo());
            stmt.setInt(6, f.getContrato().getId());
            stmt.setInt(7, f.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Factura WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Factura mapear(ResultSet rs) throws SQLException {
        int idContrato = rs.getInt("idContrato");
        Contrato contrato = contratoDAO.findId(String.valueOf(idContrato))
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado, id=" + idContrato));
        Factura f = new Factura(
                FechaSQLite.parsear(rs.getString("fechaEmision")),
                FechaSQLite.parsear(rs.getString("fechaVencimiento")),
                FechaSQLite.parsear(rs.getString("fechaCorte")),
                rs.getDouble("valorBase"),
                contrato);
        f.setId(rs.getInt("id"));
        f.setEstado(EstadoFactura.fromCodigo(rs.getInt("estado")));
        f.setValorTotal(rs.getDouble("valorTotal")); // aplica el valor real (con mora acumulada, si la hay)

        return f;
    }
}