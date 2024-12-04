const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
  // _id: { type: Number },
  userName: { type: String, required: true, unique: true },
  fullName: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  passwd: { type: String, required: true },
  // status: { type: Number, required: true },
  // date: { type: Date, required: true },
});

module.exports = mongoose.model("User", userSchema);
