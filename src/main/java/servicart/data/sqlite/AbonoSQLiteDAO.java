package servicart.data.sqlite;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Abono;
import servicart.entities.Factura;
import servicart.entities.enums.ModalidadPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbonoSQLiteDAO implements CrudDAO<Abono> {
    private final FacturaSQLiteDAO facturaDAO = new FacturaSQLiteDAO();

    @Override
    public void save(Abono a) {
        String sql = "INSERT INTO Abono (monto, fechaPago, pagoRealizado, modalidadPago, idFactura) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, a.getMonto());
            stmt.setString(2, FechaSQLiteUtil.formatear(a.getFechaPago()));
            stmt.setInt(3, a.isPagoRealizado() ? 1 : 0);
            stmt.setInt(4, a.getModalidadPago().getCodigo());
            stmt.setInt(5, a.getFactura().getId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) { if (rs.next()) a.setId(rs.getInt(1)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<Abono> findId(String id) {
        String sql = "SELECT * FROM Abono WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return Optional.of(mapear(rs)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Abono> findAll() {
        List<Abono> lista = new ArrayList<>();
        String sql = "SELECT * FROM Abono";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Abono a) {
        String sql = "UPDATE Abono SET monto=?, fechaPago=?, pagoRealizado=?, modalidadPago=?, idFactura=? WHERE id=?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, a.getMonto());
            stmt.setString(2, FechaSQLiteUtil.formatear(a.getFechaPago()));
            stmt.setInt(3, a.isPagoRealizado() ? 1 : 0);
            stmt.setInt(4, a.getModalidadPago().getCodigo());
            stmt.setInt(5, a.getFactura().getId());
            stmt.setInt(6, a.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Abono WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Abono mapear(ResultSet rs) throws SQLException {
        int idFactura = rs.getInt("idFactura");
        Factura factura = facturaDAO.findId(String.valueOf(idFactura))
                .orElseThrow(() -> new RuntimeException("Factura no encontrada, id=" + idFactura));

        Abono a = new Abono(
                rs.getDouble("monto"),
                FechaSQLiteUtil.parsear(rs.getString("fechaPago")),
                rs.getInt("pagoRealizado") == 1,
                factura,
                ModalidadPago.fromCodigo(rs.getInt("modalidadPago")));
        a.setId(rs.getInt("id"));
        return a;
    }
}