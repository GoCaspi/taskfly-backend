package registration;

public class Login {
    private String email;
    private String password;
    private String firstName;
    public String confirmPassword;

     public Login(String email,String password,String firstname,String confirmPassword){
         this.email=email;
         this.firstName=firstname;
         this.password=password;
         this.confirmPassword=confirmPassword;
     }
}
