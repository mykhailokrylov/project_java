import logo from "./logo.png";
import { Dropdown } from "react-bootstrap";
import { NavLink } from "react-router-dom";

function Header(props) {
  return (
    <nav className="navbar navbar-expand-lg navbar-light custom-navbar">
      <div className="container px-1">
        <a className="navbar-brand" href="/">
          <img src={logo} alt="Logo" className="logo-img" />
          Podstawy wędkarstwa
        </a>
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ml-auto">
            <li className="nav-item active">
              <a className="nav-link" href="/">
                Home
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/spoty">
                Wędkarskie spoty
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/fishes">
                Ryby
              </a>
            </li>
            {props.isLogged ? (
              <li className="nav-item">
                <a className="nav-link" href="/myfishes">
                  Moje Ryby
                </a>
              </li>
            ) : (
              <></>
            )}
            <li className="nav-item">
              <a className="nav-link" href="/sprzet">
                Sprzęt
              </a>
            </li>
            {props.isLogged ? (
              <li className="nav-item">
                <a href="/logout" className="nav-link">
                  Sign Out
                </a>
              </li>
            ) : (
              <></>
            )}

            <Dropdown className="nav-item">
              <Dropdown.Toggle className="nav-link">
                Login/Register
              </Dropdown.Toggle>

              <Dropdown.Menu>
                <Dropdown.Item href="/login">Login</Dropdown.Item>
                <Dropdown.Item href="/registration">Register</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>

            {/* <li className="nav-item dropdown">
              <a
                className="nav-link dropdown-toggle"
                href="#"
                id="navbarDropdown"
                role="button"
                data-toggle="dropdown"
                aria-haspopup="true"
                aria-expanded="false"
              >
                Login/Register
              </a>
              <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                <div className="dropdown-divider"></div>
                <a className="dropdown-item" href="Registration.php">
                  New around here? Sign up
                </a>
              </div>
            </li> */}
          </ul>
        </div>
      </div>
    </nav>
  );
}
export default Header;
