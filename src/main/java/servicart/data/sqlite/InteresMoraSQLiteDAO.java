package servicart.data.sqlite;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Factura;
import servicart.entities.InteresMora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InteresMoraSQLiteDAO implements CrudDAO<InteresMora> {
    private final FacturaSQLiteDAO facturaDAO = new FacturaSQLiteDAO();

    @Override
    public void save(InteresMora m) {
        String sql = "INSERT INTO InteresMora (diasRetraso, interesAcumulado, fechaCalculo, aplicadoAFactura, idFactura) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, m.getDiasRetraso());
            stmt.setDouble(2, m.getInteresAcumulado());
            stmt.setString(3, FechaSQLiteUtil.formatear(m.getFechaCalculo()));
            stmt.setInt(4, m.isAplicadoAFactura() ? 1 : 0);
            stmt.setInt(5, m.getFactura().getId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) { if (rs.next()) m.setId(rs.getInt(1)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<InteresMora> findId(String id) {
        String sql = "SELECT * FROM InteresMora WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return Optional.of(mapear(rs)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<InteresMora> findAll() {
        List<InteresMora> lista = new ArrayList<>();
        String sql = "SELECT * FROM InteresMora";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return lista;
    }

    @Override
    public void update(InteresMora m) {
        String sql = "UPDATE InteresMora SET diasRetraso=?, interesAcumulado=?, fechaCalculo=?, aplicadoAFactura=?, idFactura=? WHERE id=?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, m.getDiasRetraso());
            stmt.setDouble(2, m.getInteresAcumulado());
            stmt.setString(3, FechaSQLiteUtil.formatear(m.getFechaCalculo()));
            stmt.setInt(4, m.isAplicadoAFactura() ? 1 : 0);
            stmt.setInt(5, m.getFactura().getId());
            stmt.setInt(6, m.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM InteresMora WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private InteresMora mapear(ResultSet rs) throws SQLException {
        int idFactura = rs.getInt("idFactura");
        Factura factura = facturaDAO.findId(String.valueOf(idFactura))
                .orElseThrow(() -> new RuntimeException("Factura no encontrada, id=" + idFactura));

        InteresMora m = new InteresMora(
                rs.getInt("diasRetraso"),
                rs.getDouble("interesAcumulado"),
                FechaSQLiteUtil.parsear(rs.getString("fechaCalculo")),
                rs.getInt("aplicadoAFactura") == 1,
                factura);
        m.setId(rs.getInt("id"));
        return m;
    }
}