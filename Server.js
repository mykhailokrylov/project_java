const express = require("express");
const path = require("path");
const app = express();
const mongoose = require("mongoose");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors");

const User = require("./models/user.js");

const passport = require("./authpassport.js");

var crypto = require("crypto");

// const passport = require("passport");

// const passportJWT = require("passport-jwt");

// const JwtStrategy = passportJWT.Strategy;
// const ExtractJwt = passportJWT.ExtractJwt;

const jwt = require("jsonwebtoken");

app.use(express.json());
// app.use(express.static("dist"));

// app.get("/*", (req, res) => {
//   res.sendFile(path.join(__dirname + "/react-app/public/index.html"));
// });

// var opts = {};
// opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
// opts.secretOrKey = "SERVERSECRET";
// opts.issuer = 'accounts.examplesoft.com';
// opts.audience = 'yoursite.net';

// passport.use(
//   new JwtStrategy(opts, async function (jwt_payload, done) {
//     try {
//       console.log(jwt_payload);
//       const user = await User.findOne({ _id: jwt_payload._id });
//       console.log(user);
//       return done(null, user);
//     } catch (error) {
//       console.error(error);
//       return done(error, false);
//     }

//     //g();

//     // User.findOne({ _id: jwt_payload.sub }, function (err, user) {
//     //   if (err) {
//     //     return done(err, false);
//     //   }
//     //   if (user) {
//     //     return done(null, user);
//     //   } else {
//     //     return done(null, false);
//     //   }
//     // });
//   })
// );

// app.post(
//   "/auth",
//   passport.authenticate("jwt", { session: false }),
//   function (req, res) {
//     res.send({ res: "OK" });
//   }
// );

function hashPass(pass) {
  return crypto.createHash("md5").update(pass).digest("hex");
}

function validateUser(user) {
  if (user.userName.length < 3) {
    throw new Error("Username is too short");
  }

  if (user.passwd.length < 3) {
    throw new Error("Password is too short");
  }

  if (user.fullName.length < 3) {
    throw new Error("Full name is too short");
  }

  if (user.email.length < 3) {
    throw new Error("Email is too short");
  }

  if (user.email.indexOf("@") == -1) {
    throw new Error("Email is invalid");
  }

  if (user.email.indexOf(".") == -1) {
    throw new Error("Email is invalid");
  }

  if (user.userName.indexOf(" ") != -1) {
    throw new Error("Username cannot contain spaces");
  }
}

app.post("/api/login", async (req, res) => {
  // res.status(200).send();

  const login = req.body.login;
  const password = req.body.passwd;

  try {
    console.log("hogin:" + login);
    console.log("pass:" + password);

    const user = await User.findOne({
      userName: login,
      passwd: hashPass(password),
    });

    const sjwt = jwt.sign({ _id: user._id }, "SERVERSECRET");
    res.status(201).json({ jwt: sjwt });
  } catch (error) {
    res.status(400).json({ message: "USER NOT FOUND" });
  }
});

app.post("/api/registration", async (req, res) => {
  console.log(req.body.fullName);
  // res.status(200).send();

  const userData = new User({
    userName: req.body.userName,
    fullName: req.body.fullName,
    email: req.body.email,
    passwd: hashPass(req.body.passwd1),
  });

  try {
    validateUser(userData);
    const savedUser = await userData.save();
    const sjwt = jwt.sign({ _id: savedUser._id }, "SERVERSECRET");
    console.log(sjwt);
    res.status(201).json({ jwt: sjwt });
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

app.use(express.json());

app.listen(4000, "10.242.245.109", () => {
  console.log("App running on http://localhost:4000");
});

// MongoDB connection setup

const username = "user";
const password = "password";
const clusterUrl = "127.0.0.1:27017";

mongoose.connect(`mongodb://${username}:${password}@${clusterUrl}/mydb`, {});

const db = mongoose.connection;

db.on("error", (error) => {
  console.log(error);
});

db.once("connected", () => {
  console.log("Database Connected");
});

const fishRoutes = require("./routes/fishRoutes.js");
const userRoutes = require("./routes/userRoutes.js");

// app.get("/", (res) => {
//   res.redirect("http://google.com/");
//   // var newurl = "http://google.com/";
//   // request(newurl).pipe(res);
// });

app.use("/api/fish", fishRoutes);
app.use("/api/user", userRoutes);

app.use(cors());

app.use(
  "/",
  createProxyMiddleware({
    target: "http://10.242.245.109:3000",
    changeOrigin: false,
  })
);
