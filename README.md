# mcpai

Minimum cost path, with multiple "superimposed" graphs. Probably
A-star.

## APIs, Dependencies

Some dependencies just work -- the included jars and the Maven
stuff.

But some APIs need a key. Just use the same one for all of these, as
long as they're all enabled (step 3 below). 

1. Go to the [Google developer console], **make a project**, call it
   `aofsdijosd` or something.

2. **Create a credential**, and pick an ordinary API key.

3. Go to **the library** (on the left side), and add all these
   services to your project (_`[*]` is optional, probably_):

  + [QPX Express Airfare API](https://console.developers.google.com/apis/api/qpxexpress-json.googleapis.com/)
  + [Google Maps Distance Matrix API](https://console.developers.google.com/apis/api/distance_matrix_backend/)
  + [Google Maps Geocoding API](https://console.developers.google.com/apis/api/geocoding_backend/)
  + [Google Maps Geolocation API](https://console.developers.google.com/apis/api/geolocation/)
  + [Google Maps Directions API](https://console.developers.google.com/apis/api/directions_backend/)
  + [Google Places API Web Service](https://console.developers.google.com/apis/api/places_backend/overview?project=ogretitus)
  + `[*]` Google Places API for Android
  + `[*]` Google Maps Roads API
  + `[*]` Google Maps Elevation API
  + `[*]` Google Maps Embed API
  + `[*]` Google Maps JavaScript API
  + `[*]` Google Maps Time Zone API

[Google developer console]: https://console.developers.google.com/apis/
