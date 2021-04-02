package suranovan.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity(name = "usersTable")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    private Long id;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    private final List<Roles> roles = new ArrayList<>();

    public void addRoles(List<Roles> rolesList){
        roles.addAll(rolesList);
    }

    public void addRole(Roles role){
        roles.add(role);
    }

    public Collection<Roles> getRoles() {
        return roles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String authority) {
        this.role = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
