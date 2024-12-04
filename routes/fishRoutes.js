const express = require("express");
const router = express.Router();
const myFish = require("../models/myFish");
const passport = require("../authpassport.js");
const multer = require("multer");
const path = require("path");
const fs = require("fs");
const { readdir } = require("fs/promises");

module.exports = router;

const findByName = async (dir, name) => {
  const matchedFiles = [];

  const files = await readdir(dir);

  for (const file of files) {
    // Method 1:
    const filename = path.parse(file).name;

    if (filename === name) {
      matchedFiles.push(file);
    }
  }

  return matchedFiles;
};

// Set up storage for multer
// const storage = multer.diskStorage({
//   destination: (req, file, cb) => {
//     cb(null, "./userFishes");
//   },
//   filename: (req, file, cb) => {
//     console.log(req.user._id);
//     cb(null, req.user._id + path.extname(file.originalname));
//   },
// });

const storage = multer.memoryStorage();

// Initialize multer with the storage configuration
const upload = multer({ storage: storage });

router.get("/image/:img", async (req, res) => {
  const img = req.params.img;

  const files = await findByName("./userFishes", img);

  if (files.length > 0) {
    res.sendFile(files[0], { root: "./userFishes" });
  } else {
    res.status(404);
  }
});

// Get all Method
router.get("/getAll", async (req, res) => {
  try {
    const data = await myFish.find();
    res.json(data);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.use("/", passport.authenticate("jwt", { session: false }));

router.get("/getMy", async (req, res) => {
  try {
    const userId = req.user._id;
    const data = await myFish.find({ user_id: userId });
    console.log(data);
    res.json(data);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Post Method with image upload
router.post("/post", upload.single("image"), async (req, res) => {
  const userId = req.user._id;

  const fishData = new myFish({
    Name: req.body.Name,
    Place: req.body.Place,
    Weight: req.body.Weight,
    user_id: userId,
  });

  console.log();

  console.log("TRY SAVE:");
  console.log(fishData);

  try {
    const savedFish = await fishData.save();

    console.log(savedFish);

    if (req.file) {
      fs.writeFile(
        `./userFishes/${savedFish._id + path.extname(req.file.originalname)}`,
        req.file.buffer,
        "binary",
        (err) => {
          if (err) {
            console.log(err);
          } else {
            console.log("The file was saved!");
          }
        }
      );
    }

    res.status(201).json(savedFish);
  } catch (error) {
    console.error(error.message);
    res.status(400).json({ message: error.message });
  }
});

// Get by ID Method
router.get("/getOne/:id", async (req, res) => {
  try {
    const data = await myFish.findById(req.params.id);
    res.json(data);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Update by ID Method
router.post("/update/:id", upload.single("image"), async (req, res) => {
  try {
    const id = req.params.id;
    const options = { new: true };

    const updatedData = {
      Name: req.body.Name,
      Place: req.body.Place,
      Weight: req.body.Weight,
    };

    const result = await myFish.findByIdAndUpdate(id, updatedData, options);

    if (req.file) {
      fs.writeFile(
        `./userFishes/${id + path.extname(req.file.originalname)}`,
        req.file.buffer,
        "binary",
        (err) => {
          if (err) {
            console.log(err);
          } else {
            console.log("The file was saved!");
          }
        }
      );
    }

    res.send(result);
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

// Delete by ID Method
router.delete("/delete/:id", async (req, res) => {
  try {
    const id = req.params.id;
    console.log(id);
    const data = await myFish.findByIdAndDelete(id);
    res.status(200).json(`Image with ${id} has been deleted..`);
  } catch (error) {
    console.error(error.message);
    res.status(400).json({ message: error.message });
  }
});
