const mongoose = require("mongoose");

// MongoDB connection setup

const username = "user";
const password = "password";
const clusterUrl = "127.0.0.1:27017";
const authMechanism = "DEFAULT";

mongoose.connect(`mongodb://${username}:${password}@${clusterUrl}/mydb`, {});

const db = mongoose.connection;
db.on("error", console.error.bind(console, "connection error:"));
db.once("open", function () {
  console.log("Connected to MongoDB");

  // Define the user schema with _id as a number
  const userSchema = new mongoose.Schema({
    _id: { type: Number, required: true },
    userName: { type: String, required: true },
    fullName: { type: String, required: true },
    email: { type: String, required: true, unique: true },
    passwd: { type: String, required: true },
    status: { type: Number, required: true },
    date: { type: Date, required: true },
  });

  // Define the User model
  const User = mongoose.model("User", userSchema);

  // User data
  const users = [
    {
      _id: 2,
      userName: "confirm",
      fullName: "confirm confirm",
      email: "confirm@gmail.com",
      passwd: "$2y$10$OJCvsU5sSaQn3jDvUxQguOjEZmLOg0LPxp.TJr2d6ZhZu2LQUEeS.",
      status: 1,
      date: new Date("2024-01-23 00:00:00"),
    },
    {
      _id: 21,
      userName: "Vasiok",
      fullName: "Vasia Pup",
      email: "vasia@gmail.com",
      passwd: "$2y$10$SjhLIdQuhPoct3b3e1vTweI3wrN.Fzc5YeMPlFgTNJzLn4Ri78Tt.",
      status: 1,
      date: new Date("2024-01-14 00:00:00"),
    },
    {
      _id: 27,
      userName: "qwerty",
      fullName: "qwerty qwerty",
      email: "qwerty@podss.com",
      passwd: "$2y$10$f/vSzQOeKbfFClJ8gIrzTuiy.HKQujwnit9mo4EayrxZZrkvuDW5G",
      status: 1,
      date: new Date("2024-01-23 00:00:00"),
    },
    {
      _id: 44,
      userName: "required",
      fullName: "required required",
      email: "required@required.com",
      passwd: "$2y$10$wuuumtwJFJr4NYzuqq7HsuW2ofEujzYN3i09OhvFgqqBY6eIKAnem",
      status: 1,
      date: new Date("2024-01-25 00:00:00"),
    },
  ];

  // Insert users
  User.insertMany(users)
    .then((insertedUsers) => {
      console.log("Successfully saved users to the database.");

      // Define the fish schema with _id as a number
      const myFishSchema = new mongoose.Schema({
        _id: { type: Number, required: true },
        Name: { type: String, required: true },
        Place: { type: String, required: true },
        Weight: { type: Number, required: true },
        user_id: { type: Number, ref: "User", required: true },
      });

      // Define the MyFish model
      const MyFish = mongoose.model("MyFish", myFishSchema);

      // Fish data
      const myFishes = [
        { _id: 12, Name: "Karas", Place: "Black sea", Weight: 0.3, user_id: 2 },
        { _id: 13, Name: "wzdręga", Place: "Sasyk", Weight: 0.2, user_id: 2 },
        { _id: 14, Name: "Karaś", Place: "Sasyk", Weight: 18, user_id: 2 },
        { _id: 15, Name: "Szczupak", Place: "Dnipro", Weight: 4, user_id: 2 },
        { _id: 16, Name: "Som", Place: "Wisla", Weight: 4, user_id: 44 },
      ];

      // Insert fish records
      return MyFish.insertMany(myFishes);
    })
    .then(() => {
      console.log("Successfully saved fishes to the database.");

      // Define the logged in user schema with _id as a number
      const loggedInUserSchema = new mongoose.Schema({
        _id: { type: Number, required: true },
        userId: { type: Number, ref: "User", required: true },
        lastUpdate: { type: Date, required: true },
      });

      // Define the LoggedInUser model
      const LoggedInUser = mongoose.model("LoggedInUser", loggedInUserSchema);

      // Logged in users data (if needed for insertion)
      const loggedInUsers = [
        {
          _id: "88",
          userId: 2,
          lastUpdate: new Date("2024-01-23 01:00:00"),
        },
        {
          _id: "99",
          userId: 21,
          lastUpdate: new Date("2024-01-14 01:00:00"),
        },
        {
          _id: "111",
          userId: 27,
          lastUpdate: new Date("2024-01-23 01:00:00"),
        },
        {
          _id: "888",
          userId: 44,
          lastUpdate: new Date("2024-01-25 01:00:00"),
        },
      ];

      // Insert logged in users (if needed)
      return LoggedInUser.insertMany(loggedInUsers);
    })
    .then(() => {
      console.log("Successfully saved logged in users to the database.");
    })
    .catch((error) => {
      console.log(error);
    });
});
