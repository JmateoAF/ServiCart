package servicart.data.sqlite;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpresaSQLiteDAO implements CrudDAO<Empresa> {
    @Override
    public void save(Empresa e) {
        String sql = "INSERT INTO Empresa (nombre) VALUES (?)";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, e.getNombre());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<Empresa> findId(String id) {
        String sql = "SELECT id, nombre FROM Empresa WHERE id = ?";
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
    public List<Empresa> findAll() {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT id, nombre FROM Empresa";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) empresas.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return empresas;
    }

    @Override
    public void update(Empresa e) {
        String sql = "UPDATE Empresa SET nombre = ? WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, e.getNombre());
            stmt.setInt(2, e.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Empresa WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Empresa mapear(ResultSet rs) throws SQLException {
        Empresa empresa = new Empresa(rs.getString("nombre"));
        empresa.setId(rs.getInt("id"));
        return empresa;
    }
}