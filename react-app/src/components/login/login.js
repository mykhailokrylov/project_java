import { Form } from "react-router-dom";

function Login() {
  return (
    <Form action={"/login"} method="post" className="px-4 py-3">
      <div className="form-group">
        <label htmlFor="login">Username</label>
        <input
          type="text"
          className="form-control"
          id="username"
          name="login"
          placeholder="Username"
          required
        />
      </div>
      <div className="form-group">
        <label htmlFor="passwd">Password</label>
        <input
          type="password"
          className="form-control"
          id="password"
          name="passwd"
          placeholder="Password"
          required
        />
      </div>
      <button
        type="submit"
        value="Zaloguj"
        name="zaloguj"
        className="btn-sample"
      >
        Sign in
      </button>
    </Form>
  );
}
export default Login;
