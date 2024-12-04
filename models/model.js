const mongoose = require("mongoose");

const myFishSchema = new mongoose.Schema({
  _id: { type: Number, required: true },
  Name: { type: String, required: true },
  Place: { type: String, required: true },
  Weight: { type: Number, required: true },
  user_id: { type: Number, ref: "User", required: true },
});

module.exports = mongoose.model("myFish", myFishSchema);
