import { useState, useEffect, useMemo } from "react";

function useFetch() {
  const [JWTkey, JWTkeySet] = useState(localStorage.getItem("token"));
  const [isLogged, isLoggedSet] = useState(false);

  useMemo(() => {
    isLoggedSet(JWTkey != null && JWTkey != "");

    localStorage.setItem("token", JWTkey == null ? "" : JWTkey);
  }, [JWTkey]);

  return {
    get: request("GET"),
    post: request("POST"),
    put: request("PUT"),
    delete: request("DELETE"),
    setJWT: JWTkeySet,
    isLogged: isLogged,
  };

  function request(method) {
    return async (url, body, type = "JSON") => {
      const requestOptions = {
        method,
        headers: authHeader(),
      };
      if (body) {
        if (type == "JSON") {
          requestOptions.headers["Content-Type"] = "application/json";
          requestOptions.body = JSON.stringify(body);
        } else if ("FORM") {
          //requestOptions.headers["Content-Type"] = "multipart/form-data";
          requestOptions.body = body;
        }
      }
      return await (await fetch(url, requestOptions)).json();
    };
  }

  function authHeader() {
    if (JWTkey != "") {
      return { Authorization: `Bearer ${JWTkey}` };
    } else {
      return {};
    }
  }

  //   let postJsonData = async (url = "", data = {}) => {
  //     // Default options are marked with *
  //     const response = await fetch(url, {
  //       method: "POST", // *GET, POST, PUT, DELETE, etc.
  //       mode: "cors", // no-cors, *cors, same-origin
  //       cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
  //       credentials: "same-origin", // include, *same-origin, omit
  //       headers: {
  //         "Content-Type": "application/json",
  //         Authorization: "Bearer " + JWTkey,
  //       },
  //       redirect: "manual", // manual, *follow, error
  //       referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
  //       body: JSON.stringify(data), // body data type must match "Content-Type" header
  //     });
  //     return response.json(); // parses JSON response into native JavaScript objects
  //   };

  //   function getJsonData(url = "") {
  //     return fetch(url, {
  //       // method: "GET", // *GET, POST, PUT, DELETE, etc.
  //       mode: "cors", // no-cors, *cors, same-origin
  //       // cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
  //       // credentials: "same-origin", // include, *same-origin, omit
  //       // // headers: {
  //       // //   "Content-Type": "application/json",
  //       // //   // 'Content-Type': 'application/x-www-form-urlencoded',
  //       // // },

  //       headers: {
  //         Authorization: "Bearer " + JWTkey,
  //       },
  //       // redirect: "follow", // manual, *follow, error
  //       // referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
  //       // body: JSON.stringify(data), // body data type must match "Content-Type" header
  //     });
}

export default useFetch;
