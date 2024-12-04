import { useLoaderData } from "react-router-dom";

function Ryby() {
  //const userImages = require.context("userImages", true);

  const fishes = useLoaderData();

  return (
    <>
      <div className="container p-5 mt-2">
        <h4>Ryby złowione przez naszych użytkowników:</h4>
      </div>

      <div id="fishcontainer" className="container">
        <div className="row">
          {fishes.map((fish, index) => {
            const fileName = "api/fish/image/" + fish._id;

            return (
              <div key={index} className="col-md-4 mb-4">
                <div className="card">
                  <img
                    className="card-img-top"
                    src={fileName}
                    alt="Fish Image"
                    style={{ height: "200px", objectFit: "cover" }}
                  />

                  <div className="card-body">
                    <h5 className="card-title">{fish.Name}</h5>
                    <p className="card-text">
                      {/* ID: {fish._id}
                      <br /> */}
                      Place: {fish.Place}
                      <br />
                      Weight: {fish.Weight}
                      <br />
                    </p>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </>
  );
}

export default Ryby;
