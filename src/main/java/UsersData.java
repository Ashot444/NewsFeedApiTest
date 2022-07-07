import com.fasterxml.jackson.annotation.JsonAutoDetect;

public class UsersData {
    private String avatar;

    private String email;

    private String name;

    private String password;

    private String role;

    public UsersData(String avatar, String email, String name, String password, String role) {
        this.avatar = avatar;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }


    public UsersData() {
    }
    public String getAvatar() {
        return avatar;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
}


