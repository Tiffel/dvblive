var mymap = L.map('mapid').setView([51.0497, 13.7446], 13);
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
	attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="https://www.mapbox.com/">Mapbox</a>',
	maxZoom: 18,
	id: 'mapbox.streets',
	accessToken: 'pk.eyJ1IjoiZmFiaTEyMyIsImEiOiJjazJyeWxudzUwc2lrM2RvNGpxbDFsOG00In0.vLb0v9Fw3j39lYKISkddCA'
}).addTo(mymap);

