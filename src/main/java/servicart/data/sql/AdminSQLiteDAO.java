package servicart.data.sql;

import servicart.data.interfaces.AdminDAO;
import servicart.models.entidades.Administrador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminSQLiteDAO implements AdminDAO<Administrador> {
    @Override
    public Optional<Administrador> credenciales(String usuario, String contrasenia) {
        Administrador admin = null;
        String sql = "SELECT * FROM Administradores WHERE usuario = ? AND contrasenia = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasenia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Administrador(rs.getString("usuario"), rs.getString("contrasenia"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la BD al validar credenciales: " + e.getMessage());
        }

        return Optional.ofNullable(admin);
    }
}
