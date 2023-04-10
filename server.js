var createError = require('http-errors');
var express = require('express');
var path = require('path');
const cors = require('cors');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const http = require('http');
const axios = require('axios');
const geohash = require('ngeohash');


// var indexRouter = require('./routes/index');
// var usersRouter = require('./routes/users');
var app = express();
app.use(cors());

var clientId = '0c2d8f679e43464b963c9b032284f495';
var clientSecret = 'bddcef427f9a46749ee9f7cf0daf46e2';
var accesstoken = ''

var SpotifyWebApi = require('spotify-web-api-node');
const { query } = require('express');



// Create the api object with the credentials
var spotifyApi = new SpotifyWebApi({
  clientId: clientId,
  clientSecret: clientSecret
});
/*
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
*/
//app.use(express.static("csci571-hw8"))

// app.get('/', (req, res) => {
//   res.send('Hello World!');
// });

function getSegmentId(abc){
        var segmentID1
        if(abc == "Music"){
            segmentID1 = "KZFzniwnSyZfZ7v7nJ"
          }
        if(abc == "Sports"){
            segmentID1 = "KZFzniwnSyZfZ7v7nE"
          }
        if(abc == "Arts&Theatre "){
            segmentID1 = "KZFzniwnSyZfZ7v7na"
          }
          if(abc == "Arts"){
            segmentID1 = "KZFzniwnSyZfZ7v7na"
          }
        if(abc == "Theatre"){
            segmentID1 = "KZFzniwnSyZfZ7v7na"
          }
        if(abc == "Film"){
            segmentID1 = "KZFzniwnSyZfZ7v7nn"
          }
        if(abc == "Miscellaneous"){
            segmentID1 = "KZFzniwnSyZfZ7v7n1"
          }
        if(abc == "All"){
          segmentID1 = " "
          }
        return segmentID1
}

app.get('/api/form', (req, res) => {
  var keyword = req.query.keyword;
  var distance = req.query.distance;
  var latitude = req.query.latitude;
  var longitude = req.query.longitude;
  var selected_cat = req.query.selected_cat;
  var hash_location = geohash.encode(latitude, longitude);

  console.log(`Data Received: keyword=${keyword}, distance=${distance}, latitude${latitude} longitude=${longitude}, selected_cat=${selected_cat}`);
  var segmentIdd = getSegmentId(selected_cat)
  if(segmentIdd == " "){
    ticketmaster_url = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey=GbVOaAsu05gFJhOFyRgd5jVarkvGvm8M&keyword='+keyword+'&radius='+distance+'&unit=miles&geoPoint='+hash_location+''
    console.log("Hello",ticketmaster_url)
  }
  else{
    ticketmaster_url = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey=GbVOaAsu05gFJhOFyRgd5jVarkvGvm8M&keyword='+keyword+'&segmentId='+segmentIdd+'&radius='+distance+'&unit=miles&geoPoint='+hash_location+''
    console.log("Hello",ticketmaster_url)
  }
  axios.get(ticketmaster_url)   /*https://blog.logrocket.com*/
  .then(function (response) {
    res.send(response.data)
  })
  .catch(function (error) {
    console.log(error);
  }); 
  
  
  //res.send()
  // res.send(`Received form data hello from server:  keyword=${keyword}, distance=${distance}, location=${location}, selected_cat=${selected_cat}`);
});

app.get('/api/event', (req, res) => {
  var event_Id = req.query.event_id;
  console.log(`Data Received: Event-ID=${event_Id}`);
  ticketmaster_events_url = 'https://app.ticketmaster.com/discovery/v2/events/'+event_Id+'?apikey=GbVOaAsu05gFJhOFyRgd5jVarkvGvm8M'
  axios.get(ticketmaster_events_url)   /*https://blog.logrocket.com*/
  .then(function (response) {
    res.send(response.data)
  })
  .catch(function (error) {
    console.log(error);
  }); 
  
  
  
});

app.get('/api/venue', (req, res) => {
  var venue_name = req.query.venue_name;
  console.log(`Data Received: venue-name=${venue_name}`);
  ticketmaster_venue_url = 'https://app.ticketmaster.com/discovery/v2/venues?apikey=GbVOaAsu05gFJhOFyRgd5jVarkvGvm8M&keyword='+venue_name+''
  axios.get(ticketmaster_venue_url)   /*https://blog.logrocket.com*/
  .then(function (response) {
    res.send(response.data)
  })
  .catch(function (error) {
    console.log(error);
  }); 
  
  
  
});

app.get('/api/spotify', (req, res) => {
  var artist_name = req.query.artist_name;
  console.log(`Data Received: (server) Artist-Name=${artist_name}`);
  spotifyApi.searchArtists(artist_name)
      .then(function(data) {
        res.send(data.body)
      }, function(err) {
          console.log(err['statusCode'])
          if(err['statusCode']=='401'){
            spotifyApi.clientCredentialsGrant().then(
              (data) => {
                console.log('The access token expires in ' + data.body['expires_in']);
                console.log('The access token is ' + data.body['access_token']);
            
                // Save the access token so that it's used in future calls
                this.accesstoken = data.body['access_token']
                spotifyApi.setAccessToken(this.accesstoken);
                spotifyApi.searchArtists(artist_name)
                .then(function(data) {
                  console.log('Search artists by',this.artist_name, data.body);
                  res.send(data.body)
                }, function(err) {
                  console.error(err);
                });
              },
              function(err) {
                console.log('Something went wrong when retrieving an access token', err);
              }
            );
          }
      });
  
});

app.get('/api/spotifyAlbums', (req, res) => {
  var artist_id = req.query.artist_id;
  console.log(`Data Received: (server) Artist-Id=${artist_id}`);
  spotifyApi.clientCredentialsGrant().then(
    (data) => {
      console.log('The access token expires in ' + data.body['expires_in']);
      console.log('The access token is ' + data.body['access_token']);
  
      // Save the access token so that it's used in future calls
      this.accesstoken = data.body['access_token']
      spotifyApi.setAccessToken(this.accesstoken);
      spotifyApi.getArtistAlbums(artist_id)
      .then(function(data) {
        res.send(data.body)
      }, function(err) {
          console.log(err['statusCode'])
      });
      
    },
    function(err) {
      console.log('Something went wrong when retrieving an access token', err);
    }
  );
});

app.get('/api/dynamic', (req, res) => {
  var keywordss = req.query.keywordss;
  console.log(`Data Received -dynamic: key=${keywordss}`);
  ticketmaster_autocomplete_url = 'https://app.ticketmaster.com/discovery/v2/suggest?apikey=GbVOaAsu05gFJhOFyRgd5jVarkvGvm8M&keyword='+ keywordss
  axios.get(ticketmaster_autocomplete_url)   /*https://blog.logrocket.com*/
  .then(function (response) {
    res.send(response.data)
  })
  .catch(function (error) {
    console.log(error);
  }); 
  
  
  
});


const root = require("path").join(__dirname, "dist");
app.use(express.static(root));

app.get("*", (req, res) => {
  res.sendFile("index.html", { root });
});

const port = process.env.PORT || 3000;
app.listen(port,()=>{
  console.log('Node Server started on port', port);
})

  