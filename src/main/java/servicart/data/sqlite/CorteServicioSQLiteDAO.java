package servicart.data.sqlite;

import servicart.data.FechaSQLite;
import servicart.data.interfaces.CrudDAO;
import servicart.entities.Contrato;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;
import servicart.entities.enums.EstadoCorte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CorteServicioSQLiteDAO implements CrudDAO<CorteServicio> {
    private final ContratoSQLiteDAO contratoDAO = new ContratoSQLiteDAO();
    private final FacturaSQLiteDAO facturaDAO = new FacturaSQLiteDAO();

    @Override
    public void save(CorteServicio c) {
        String sql = "INSERT INTO CorteServicio (fechaCorte, fechaReactivacion, costoReactivacionPagado, estadoCorte, idContrato, idFactura) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, FechaSQLite.formatear(c.getFechaCorte()));
            stmt.setString(2, c.getFechaReactivacion() != null ? FechaSQLite.formatear(c.getFechaReactivacion()) : null);
            stmt.setDouble(3, c.getCostoReactivacionPagado());
            stmt.setInt(4, c.getEstadoCorte().getCodigo());
            stmt.setInt(5, c.getContrato().getId());
            stmt.setInt(6, c.getFactura().getId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<CorteServicio> findId(String id) {
        String sql = "SELECT * FROM CorteServicio WHERE id = ?";
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
    public List<CorteServicio> findAll() {
        List<CorteServicio> lista = new ArrayList<>();
        // Excluye TERMINADO(2), incluye ACTIVO(0) y CORTADO(1)
        String sql = "SELECT * FROM CorteServicio WHERE estadoCorte != 2";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return lista;
    }

    @Override
    public void update(CorteServicio c) {
        String sql = "UPDATE CorteServicio SET fechaCorte=?, fechaReactivacion=?, costoReactivacionPagado=?, estadoCorte=?, idContrato=?, idFactura=? WHERE id=?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, FechaSQLite.formatear(c.getFechaCorte()));
            stmt.setString(2, c.getFechaReactivacion() != null ? FechaSQLite.formatear(c.getFechaReactivacion()) : null);
            stmt.setDouble(3, c.getCostoReactivacionPagado());
            stmt.setInt(4, c.getEstadoCorte().getCodigo());
            stmt.setInt(5, c.getContrato().getId());
            stmt.setInt(6, c.getFactura().getId());
            stmt.setInt(7, c.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    // Borrado lógico: pasa a TERMINADO (código 2)
    @Override
    public void delete(String id) {
        String sql = "UPDATE CorteServicio SET estadoCorte = 2 WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private CorteServicio mapear(ResultSet rs) throws SQLException {
        int idContrato = rs.getInt("idContrato");
        int idFactura = rs.getInt("idFactura");

        Contrato contrato = contratoDAO.findId(String.valueOf(idContrato))
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado, id=" + idContrato));

        Factura factura = facturaDAO.findId(String.valueOf(idFactura))
                .orElseThrow(() -> new RuntimeException("Factura no encontrada, id=" + idFactura));

        CorteServicio c = new CorteServicio(
                FechaSQLite.parsear(rs.getString("fechaCorte")),
                contrato,
                factura);
        c.setId(rs.getInt("id"));

        String fechaReactivacionStr = rs.getString("fechaReactivacion");
        if (fechaReactivacionStr != null) {
            c.setFechaReactivacion(FechaSQLite.parsear(fechaReactivacionStr));
        }
        c.setCostoReactivacionPagado(rs.getDouble("costoReactivacionPagado"));
        c.setEstadoCorte(EstadoCorte.fromCodigo(rs.getInt("estadoCorte")));
        return c;
    }
}