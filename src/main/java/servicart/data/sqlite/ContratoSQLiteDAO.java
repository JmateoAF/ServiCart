package servicart.data.sqlite;

import servicart.entities.Contrato;
import servicart.entities.Cliente;
import servicart.entities.ServicioCatalogo;
import servicart.entities.enums.CausaTerminacion;
import servicart.data.interfaces.CrudDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContratoSQLiteDAO implements CrudDAO<Contrato> {
    private final ClienteSQLiteDAO clienteDAO = new ClienteSQLiteDAO();
    private final ServicioCatalogoSQLiteDAO servicioDAO = new ServicioCatalogoSQLiteDAO();

    @Override
    public void save(Contrato c) {
        String sql = "INSERT INTO Contrato (fechaInicio, fechaFin, causaTerminacion, idServicio, idCliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(c.getFechaInicio()));
            stmt.setTimestamp(2, c.getFechaFin() != null ? Timestamp.valueOf(c.getFechaFin()) : null);
            stmt.setInt(3, c.getCausaTerminacion().getCodigo());
            stmt.setInt(4, c.getServicio().getId());
            stmt.setString(5, c.getCliente().getCedula());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<Contrato> findId(String id) {
        String sql = "SELECT * FROM Contrato WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return Optional.of(mapear(rs)); }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Contrato> findAll() {
        List<Contrato> contratos = new ArrayList<>();
        // Mismo criterio que ContratoBinarioDAO.isActivo(): solo contratos vigentes
        String sql = "SELECT * FROM Contrato WHERE causaTerminacion = 0";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) contratos.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return contratos;
    }

    @Override
    public void update(Contrato c) {
        String sql = "UPDATE Contrato SET fechaInicio=?, fechaFin=?, causaTerminacion=?, idServicio=?, idCliente=? WHERE id=?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(c.getFechaInicio()));
            stmt.setTimestamp(2, c.getFechaFin() != null ? Timestamp.valueOf(c.getFechaFin()) : null);
            stmt.setInt(3, c.getCausaTerminacion().getCodigo());
            stmt.setInt(4, c.getServicio().getId());
            stmt.setString(5, c.getCliente().getCedula());
            stmt.setInt(6, c.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        // Igual que en Binario: "terminar" contrato es responsabilidad del Service vía update().
        // Este método solo cumple el contrato de CrudDAO (borrado físico), no se usa para cancelar.
        String sql = "DELETE FROM Contrato WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Contrato mapear(ResultSet rs) throws SQLException {
        Cliente cliente = clienteDAO.findId(rs.getString("idCliente"))
                .orElseThrow(() -> {
                    try {
                        return new RuntimeException("Cliente no encontrado para el contrato " + rs.getInt("id"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        ServicioCatalogo servicio = servicioDAO.findId(String.valueOf(rs.getInt("idServicio")))
                .orElseThrow(() -> {
                    try {
                        return new RuntimeException("Servicio no encontrado para el contrato " + rs.getInt("id"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        LocalDateTime fechaInicio = rs.getTimestamp("fechaInicio").toLocalDateTime();
        Timestamp fechaFinTs = rs.getTimestamp("fechaFin");
        LocalDateTime fechaFin = fechaFinTs != null ? fechaFinTs.toLocalDateTime() : null;

        Contrato contrato = new Contrato(fechaInicio, fechaFin, CausaTerminacion.fromCodigo(rs.getInt("causaTerminacion")), servicio, cliente);
        contrato.setId(rs.getInt("id"));
        return contrato;
    }
}