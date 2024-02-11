package User;

import Annotations.HtmlForm;
import Annotations.HtmlInput;

@HtmlForm(fileName = "user_form.html", action = "/users", method = "post")
public class UserForm {
    @HtmlInput(type = "text", name = "Amur", placeholder = "Enter First Name")
    private String firstName;

    @HtmlInput(type = "text", name = "Po", placeholder = "Enter Last Name")
    private String lastName;

    @HtmlInput(type = "password", name = "password", placeholder = "Enter Password")
    private String password;
}
