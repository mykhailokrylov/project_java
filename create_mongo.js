var MongoClient = require("mongodb").MongoClient;
// var url = "mongodb://127.0.0.1:27017/";

// Replace the following with values for your environment.
const username = "user";
const password = "password";
const clusterUrl = "127.0.0.1:27017";
const authMechanism = "DEFAULT";
// Replace the following with your MongoDB deployment's connection string.
const uri = `mongodb://${username}:${password}@${clusterUrl}/mydb`;

const client = new MongoClient(uri);

client.on("open", () => {
  console.log("connected");
});

client.on("timeout", () => {
  console.log("timeout");
});

client.on("close", () => {
  console.log("close");
});

client.on("commandSucceed", () => {
  console.log("close");
});

client.connect();
client.a;

const db = client.db();

db.collection("mycoll").insertOne({ name: "viktor" });

// MongoClient.connect(url, function (err, db) {
//   if (err) throw err;
//   console.log("Database created!");
//   db.close();
// });
