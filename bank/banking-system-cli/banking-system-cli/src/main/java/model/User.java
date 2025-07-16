package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String idProof; // PAN or Aadhaar

    public User(int id, String name, String email, String password, String idProof) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.idProof = idProof;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIdProof() { // ✅ Correct method
        return idProof;
    }
}
