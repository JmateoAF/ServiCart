package servicart.data.sqlite;

import servicart.entities.ServicioCatalogo;
import servicart.entities.Empresa;
import servicart.entities.enums.TipoServicio;
import servicart.entities.enums.TipoValorFactura;
import servicart.data.interfaces.CrudDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicioCatalogoSQLiteDAO implements CrudDAO<ServicioCatalogo> {
    private final EmpresaSQLiteDAO empresaDAO = new EmpresaSQLiteDAO();

    @Override
    public void save(ServicioCatalogo s) {
        String sql = "INSERT INTO ServicioCatalogo (idEmpresa, tipoServicio, tipoValor, tarifaFija, tarifaPorUnidad, costoReactivacion, tasaInteresDiario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, s.getEmpresa().getId());
            stmt.setInt(2, s.getTipo().getCodigo());
            stmt.setInt(3, s.getTipoValor().getCodigo());
            stmt.setDouble(4, s.getTarifaFija());
            stmt.setDouble(5, s.getTarifaPorUnidad());
            stmt.setDouble(6, s.getCostoReactivacion());
            stmt.setDouble(7, s.getTasaInteresDiario());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<ServicioCatalogo> findId(String id) {
        String sql = "SELECT * FROM ServicioCatalogo WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return Optional.of(mapear(rs)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<ServicioCatalogo> findAll() {
        List<ServicioCatalogo> servicios = new ArrayList<>();
        String sql = "SELECT * FROM ServicioCatalogo";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) servicios.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return servicios;
    }

    @Override
    public void update(ServicioCatalogo s) {
        String sql = "UPDATE ServicioCatalogo SET idEmpresa=?, tipoServicio=?, tipoValor=?, tarifaFija=?, tarifaPorUnidad=?, costoReactivacion=?, tasaInteresDiario=? WHERE id=?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, s.getEmpresa().getId());
            stmt.setInt(2, s.getTipo().getCodigo());
            stmt.setInt(3, s.getTipoValor().getCodigo());
            stmt.setDouble(4, s.getTarifaFija());
            stmt.setDouble(5, s.getTarifaPorUnidad());
            stmt.setDouble(6, s.getCostoReactivacion());
            stmt.setDouble(7, s.getTasaInteresDiario());
            stmt.setInt(8, s.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM ServicioCatalogo WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private ServicioCatalogo mapear(ResultSet rs) throws SQLException {
        Empresa empresa = empresaDAO.findId(String.valueOf(rs.getInt("idEmpresa")))
                .orElseThrow(() -> {
                    try {
                        return new RuntimeException("Empresa no encontrada para el servicio " + rs.getInt("id"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        ServicioCatalogo servicio = new ServicioCatalogo(
                empresa,
                TipoServicio.fromCodigo(rs.getInt("tipoServicio")),
                TipoValorFactura.fromCodigo(rs.getInt("tipoValor")),
                rs.getDouble("costoReactivacion"),
                rs.getDouble("tasaInteresDiario")
        );
        servicio.setTarifaFija(rs.getDouble("tarifaFija"));
        servicio.setTarifaPorUnidad(rs.getDouble("tarifaPorUnidad"));
        servicio.setId(rs.getInt("id"));
        return servicio;
    }
}