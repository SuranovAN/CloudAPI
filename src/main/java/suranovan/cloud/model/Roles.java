package suranovan.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "rolesTable")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roles implements GrantedAuthority {
    @Id
    private int id;
    private String role;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return id == roles.id && Objects.equals(role, roles.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}

