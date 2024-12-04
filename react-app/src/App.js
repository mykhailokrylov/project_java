import "bootstrap/dist/css/bootstrap.min.css";
import $ from "jquery";
import Popper from "popper.js";
// import "bootstrap/dist/js/bootstrap.bundle.min.js";

import logo from "./logo.svg";

import { useEffect } from "react";

import "./App.css";
import "./style.css";
import "./ryby.css";
import Header from "./components/header/header";
import Footer from "./components/footer/footer";
import Spoty from "./components/spoty/spoty";
import Sprzet from "./components/sprzet/sprzet";
import Ryby from "components/ryby/ryby";
import MojeRyby from "components/mojeryby/mojeRyby";
import EditRyby from "components/mojeryby/edit";
import Registration from "components/Registration/registration";
import Login from "components/login/login";
import Home from "components/home/home";
import "bootstrap/dist/css/bootstrap.min.css";

// import "bootstrap/dist/js/bootstrap.bundle.min.js";

import useFetch from "components/useFetch/useFetch";

import {
  createBrowserRouter,
  RouterProvider,
  redirect,
} from "react-router-dom";

function App() {
  const fetchWrap = useFetch();

  // async function registrationAction({ request, params }) {
  //   const formData = await request.formData();
  //   const data = Object.fromEntries(formData);

  //   console.log(data);

  //   fetchWrap.post(`/api/registration`, data).then(
  //     (resp) => {
  //       console.log("OK");
  //       console.log(resp.jwt);
  //       fetchWrap.setJWT(resp.jwt);
  //     },
  //     () => {
  //       console.log("FAIL");
  //     }
  //   );
  //   return null;
  // }

  // async function loginAction({ request, params }) {
  //   const formData = await request.formData();
  //   const data = Object.fromEntries(formData);

  //   fetchWrap.post(`/api/login`, data).then(
  //     (resp) => {
  //       console.log("OK");
  //       console.log(resp.jwt);
  //       fetchWrap.setJWT(resp.jwt);
  //     },
  //     () => {
  //       console.log("FAIL");
  //     }
  //   );

  //   return null;
  // }

  async function fishesLoader() {
    return await fetchWrap.get(`/api/fish/getAll`);
  }

  const router = createBrowserRouter([
    {
      path: "/",
      element: <Home />,
    },
    {
      path: "registration",
      element: <Registration />,
      action: async ({ request, params }) => {
        const formData = await request.formData();
        const data = Object.fromEntries(formData);

        console.log(data);

        fetchWrap.post(`/api/registration`, data).then(
          (resp) => {
            console.log("OK");
            console.log(resp.jwt);
            fetchWrap.setJWT(resp.jwt);
          },
          () => {
            console.log("FAIL");
          }
        );

        return redirect("/myfishes");
      },
    },
    {
      path: "login",
      element: <Login />,
      action: async ({ request, params }) => {
        const formData = await request.formData();
        const data = Object.fromEntries(formData);

        fetchWrap.post(`/api/login`, data).then(
          (resp) => {
            console.log("OK");
            console.log(resp.jwt);
            fetchWrap.setJWT(resp.jwt);
          },
          () => {
            console.log("FAIL");
          }
        );

        return redirect("/myfishes");
      },
    },
    {
      path: "logout",
      loader: () => {
        if (fetchWrap.isLogged) fetchWrap.setJWT(null);
        return redirect("/login");
      },
    },
    {
      path: "fishes",
      element: <Ryby />,
      loader: async () => {
        return await fetchWrap.get(`/api/fish/getAll`);
      },
    },
    {
      path: "myfishes",
      element: <MojeRyby />,
      loader: async () => {
        return await fetchWrap.get(`/api/fish/getMy`);
      },
    },
    {
      path: "myfishes/edit",
      loader: async () => {
        return redirect("/myfishes/edit/-1");
      },
    },
    {
      path: "myfishes/edit/add",
      action: async ({ request, params }) => {
        const formData = await request.formData();
        // const data = Object.fromEntries(formData);

        // console.log("try ADD:");
        // console.log(data);

        await fetchWrap.post(`/api/fish/post`, formData, "FORM").then(
          (resp) => {
            console.log("OK");
            console.log(resp);
          },
          () => {
            console.log("FAIL");
          }
        );
        return redirect("/myfishes");
      },
    },
    {
      path: "myfishes/edit/:fishId",
      element: <EditRyby />,
      loader: async ({ params }) => {
        const fishId = params.fishId ? params.fishId : -1;
        let fish = null;

        if (fishId != -1)
          fish = await fetchWrap.get(`/api/fish/getOne/${fishId}`).then(
            (resp) => {
              return resp;
            },
            () => {
              console.log("FAIL");
            }
          );

        return { id: fishId, fish: fish };
      },
      action: async ({ request, params }) => {
        const formData = await request.formData();
        // const data = Object.fromEntries(formData);

        const fishId = params.fishId ? params.fishId : "";

        fetchWrap.post(`/api/fish/update/${fishId}`, formData, "FORM").then(
          (resp) => {
            console.log("OK");
            console.log(resp);
          },
          (err) => {
            console.log("FAIL");
            console.log(err);
          }
        );
        return redirect("/myfishes");
      },
    },
    {
      path: "myfishes/delete/:fishId",
      action: async ({ request, params }) => {
        const fishId = params.fishId ? params.fishId : "";

        fetchWrap.delete(`/api/fish/delete/${fishId}`).then(
          (resp) => {
            console.log("OK");
            console.log(resp);
          },
          () => {
            console.log("FAIL");
          }
        );
        return redirect("/myfishes");
      },
    },

    {
      path: "spoty",
      element: <Spoty />,
    },
    {
      path: "sprzet",
      element: <Sprzet />,
    },
  ]);

  return (
    // <div className="App">
    //   <header className="App-header">
    //     <img src={logo} className="App-logo" alt="logo" />
    //     <p>
    //       Edit <code>src/App.js</code> and save to reload.
    //     </p>
    //     <a
    //       className="App-link"
    //       href="https://reactjs.org"
    //       target="_blank"
    //       rel="noopener noreferrer"
    //     >
    //       Learn React
    //     </a>
    //   </header>
    // </div>
    // <>
    //   <Header isLogged={true} />
    //   {/* <Spoty />
    //   <Sprzet />
    //   <Ryby />*/}

    //   <Registration />
    //   <Footer />
    // </>
    <>
      <Header isLogged={fetchWrap.isLogged} />

      <RouterProvider router={router} />

      <Footer />
    </>

    //     <DataBrowserRouter>
    //   <Route
    //     path="/projects"
    //     element={<ProjectsLayout />}
    //     action={ProjectsLayout.action}
    //   >
    //     <Route
    //       path=":projectId"
    //       element={<ProjectsPage />}
    //       action={ProjectsPage.action}
    //     />
    //   </Route>
    // </DataBrowserRouter>;
  );
}

export default App;
