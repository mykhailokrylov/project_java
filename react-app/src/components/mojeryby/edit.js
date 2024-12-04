import { useLoaderData, Form } from "react-router-dom";
import { useState } from "react";

function EditRyby() {
  const { id, fish } = useLoaderData();

  const [fishId, setId] = useState(id);

  let firstState = {
    name: "",
    place: "",
    weight: "",
    img: "/api/fish/image/" + 10,
  };

  if (fish) {
    firstState = {
      name: fish.Name,
      place: fish.Place,
      weight: fish.Weight,
      img: "/api/fish/image/" + fishId,
    };
  }

  const [preview, setPreview] = useState(firstState);

  const modifyPreview = (obj) => {
    console.log("MOD");
    setPreview({ ...preview, ...obj });
  };

  return (
    <>
      <div className="container p-2 mt-2">
        <h4>Ryby złowione przez naszych użytkowników:</h4>
      </div>

      <div className="container">
        <div className="row align-items-end">
          <Form
            method="post"
            encType="multipart/form-data"
            action={id != -1 ? `` : "/myfishes/edit/add"}
            className="d-flex col flex-column row-gap-2"
          >
            <div className="form-group">
              <input
                type="text"
                className="form-control"
                name="Name"
                placeholder="Fish name"
                value={preview.name}
                required
                onChange={(ch) => modifyPreview({ name: ch.target.value })}
              />
            </div>
            <div className="form-group">
              <input
                type="text"
                className="form-control"
                name="Place"
                placeholder="Place"
                value={preview.place}
                required
                onChange={(ch) => modifyPreview({ place: ch.target.value })}
              />
            </div>
            <div className="form-group">
              <input
                type="text"
                className="form-control"
                name="Weight"
                placeholder="Weight"
                value={preview.weight}
                required
                onChange={(ch) => modifyPreview({ weight: ch.target.value })}
              />
            </div>
            <div className="form-group">
              <input
                type="file"
                className="form-control"
                name="image"
                // required
                onChange={(ch) => {
                  var reader = new FileReader();

                  reader.addEventListener(
                    "load",
                    () => {
                      preview.src = reader.result;
                      modifyPreview({ img: reader.result });
                    },
                    false
                  );

                  reader.readAsDataURL(ch.target.files[0]);
                }}
              />
            </div>
            <button type="submit" className="btn btn-custom" name="add">
              Add Fish
            </button>
          </Form>
          <div className="col-md-4">
            <div className="card">
              <img
                className="card-img-top"
                src={preview.img}
                alt="Fish Image"
                style={{ height: "200px", objectFit: "cover" }}
              />

              <div className="card-body">
                <h5 className="card-title">{preview.name}</h5>
                <p className="card-text">
                  {/* ID: {"fish._id"}
                  <br /> */}
                  Place: {preview.place}
                  <br />
                  Weight: {preview.weight}
                  <br />
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default EditRyby;
