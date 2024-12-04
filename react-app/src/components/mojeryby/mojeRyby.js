import { useLoaderData, Form, useNavigate } from "react-router-dom";
import { useState } from "react";
import useFetch from "components/useFetch/useFetch";

function MojeRyby() {
  const fishes = useLoaderData();

  const fetch = useFetch();

  const navigate = useNavigate();

  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    Name: "",
    Place: "",
    Weight: "",
    //user_id: "", // Assuming you need to add the user_id
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    // Replace this URL with your actual API endpoint
    const response = await fetch("/api/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (response.ok) {
      const newFish = await response.json();
      // Optionally update the state to add the new fish to the list
      // This assumes `fishes` is a state variable
      // setFishes([...fishes, newFish]);
    } else {
      // Handle error
      console.error("Failed to add fish");
    }
    setShowForm(false);
  };

  const handleDelete = async (id) => {
    await fetch.delete(`/api/fish/delete/${id}`).then(
      (resp) => {
        console.log("OK");
        console.log(resp);
      },
      () => {
        console.log("FAIL");
      }
    );
  };

  const handleUpdate = (fish) => {
    setFormData({
      Name: fish.Name,
      Place: fish.Place,
      Weight: fish.Weight,
      //user_id: fish.user_id, // Assuming user_id is part of fish data
    });
    setShowForm(true);
  };

  return (
    <>
      <div className="container p-5 mt-2">
        <button className="btn btn-primary" onClick={() => navigate("edit/")}>
          Add Fish
        </button>
      </div>

      {showForm && (
        <div className="container">
          <h4>{formData._id ? "Update Fish" : "Add New Fish"}</h4>
          <form onSubmit={handleFormSubmit}>
            <div className="form-group">
              <label htmlFor="name">Name:</label>
              <input
                type="text"
                className="form-control"
                id="name"
                name="Name"
                value={formData.Name}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="place">Place:</label>
              <input
                type="text"
                className="form-control"
                id="place"
                name="Place"
                value={formData.Place}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="weight">Weight:</label>
              <input
                type="number"
                className="form-control"
                id="weight"
                name="Weight"
                value={formData.Weight}
                onChange={handleInputChange}
                required
              />
            </div>
            {/* <div className="form-group">
              <label htmlFor="user_id">User ID:</label>
              <input
                type="text"
                className="form-control"
                id="user_id"
                name="user_id"
                value={formData.user_id}
                onChange={handleInputChange}
                required
              />
            </div> */}
            <button type="submit" className="btn btn-success">
              {formData._id ? "Update" : "Submit"}
            </button>
          </form>
        </div>
      )}

      <div id="fishcontainer" className="container">
        <div className="row">
          {fishes.map
            ? fishes.map((fish, index) => {
                const fileName = "api/" + "fish/image/" + fish._id;

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
                        <Form method="post" action={`delete/${fish._id}`}>
                          <button
                            type="submit"
                            className="btn btn-danger"
                            // onClick={() => handleDelete(fish._id)}
                          >
                            Delete Fish
                          </button>
                        </Form>
                        <button
                          className="btn btn-secondary"
                          onClick={() => navigate(`edit/${fish._id}`)}
                        >
                          Update Fish
                        </button>
                      </div>
                    </div>
                  </div>
                );
              })
            : null}
        </div>
      </div>
    </>
  );
}

export default MojeRyby;
