package gaming;




public class User extends SystemEntity {

    private String username;
    private String password;
    private String role; // تم تحويلها إلى String لتطابق قاعدة البيانات ('Admin' أو 'Employee')


    public User(int id, String username, String password, String role) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public void validateConstraints() throws SystemDatabaseException {
        applyLibyanStandardVariableConstraint("Username", this.username);
        applyLibyanStandardVariableConstraint("Password", this.password);
    }
}