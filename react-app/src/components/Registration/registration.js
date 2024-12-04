import { Form } from "react-router-dom";
import { useState, useEffect } from "react";

function Registration() {
  const [userName, setUserName] = useState("");
  const [fullName, setFullName] = useState("");
  const [passwd1, setPasswd1] = useState("");
  const [passwd2, setPasswd2] = useState("");
  const [email, setEmail] = useState("");
  const [formErrors, setFormErrors] = useState({});

  const validateForm = () => {
    let errors = {};
    if (!/^[\w-]+$/.test(userName)) {
      errors.userName = "Username is invalid";
    }
    if (!/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/.test(email)) {
      errors.email = "Email is invalid";
    }
    if (passwd1 !== passwd2) {
      errors.password = "Passwords do not match";
    }
    setFormErrors(errors);
  };

  useEffect(() => {
    validateForm();
  }, [userName, fullName, passwd1, passwd2, email]);

  return (
    <div className="container p-5 mt-2">
      <h3>Formularz rejestracji</h3>

      <Form
        action="/registration"
        onSubmit={(ev) =>
          Object.keys(formErrors).length == 0 ? true : ev.preventDefault()
        }
        method="post"
      >
        Nazwa użytkownika: <br />
        <input
          name="userName"
          required
          onChange={(e) => setUserName(e.target.value)}
        />
        {formErrors.userName && <div>{formErrors.userName}</div>}
        <br />
        Imię i nazwisko: <br />
        <input
          name="fullName"
          required
          onChange={(e) => setFullName(e.target.value)}
        />
        <br />
        Hasło: <br />
        <input
          type="password"
          name="passwd1"
          required
          onChange={(e) => setPasswd1(e.target.value)}
        />
        <br />
        Potwierdź hasło: <br />
        <input
          type="password"
          name="passwd2"
          required
          onChange={(e) => setPasswd2(e.target.value)}
        />
        {formErrors.password && <div>{formErrors.password}</div>}
        <br />
        Adres e-mail: <br />
        <input name="email" onChange={(e) => setEmail(e.target.value)} />
        {formErrors.email && <div>{formErrors.email}</div>}
        <br />
        <br />
        <input type="submit" name="submit" value="Rejestruj" />
        <input type="reset" value="Anuluj" />
      </Form>
    </div>
  );
}

export default Registration;
