const passport = require("passport");
const passportJWT = require("passport-jwt");

const JwtStrategy = passportJWT.Strategy;
const ExtractJwt = passportJWT.ExtractJwt;

const User = require("./models/user.js");

var opts = {};
opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
opts.secretOrKey = "SERVERSECRET";

passport.use(
  new JwtStrategy(opts, async function (jwt_payload, done) {
    try {
      //console.log(jwt_payload);
      const user = await User.findOne({ _id: jwt_payload._id });
      //console.log(user);
      return done(null, user);
    } catch (error) {
      console.error(error);
      return done(error, false);
    }

    //g();

    // User.findOne({ _id: jwt_payload.sub }, function (err, user) {
    //   if (err) {
    //     return done(err, false);
    //   }
    //   if (user) {
    //     return done(null, user);
    //   } else {
    //     return done(null, false);
    //   }
    // });
  })
);

module.exports = passport;
