package servicart.data.sql;

import servicart.domain.models.Usuario;
import servicart.domain.interfaces.CrudDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioSQLiteDAO implements CrudDAO<Usuario> {
    @Override
    public void save(Usuario usuario) {
    }

    //@Override
    //public Optional<Usuario> findId(String id){
    //}

    //@Override
    //public List<Usuario> findAll(){
    //}

    @Override
    public void update(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, cedula = ? WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCedula());
            stmt.setInt(3, usuario.getId());
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id){
    }
}
